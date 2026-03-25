package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.AddChargeRequest;
import com.example.mpr_backend_jan.dto.ChargeBreakdownResponse;
import com.example.mpr_backend_jan.dto.ChargeBreakdownResponse.ChargeLineItem;
import com.example.mpr_backend_jan.dto.UpdateChargeRequest;
import com.example.mpr_backend_jan.model.*;
import com.example.mpr_backend_jan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargeManagementService {

    private final SocietyChargeRepository societyChargeRepository;
    private final ChargeTypeRepository chargeTypeRepository;
    private final UserRepository userRepository;

    // -------------------------------------------------------
    // READ — bifurcation + total for a society
    // -------------------------------------------------------

    /**
     * Returns ALL charges (active + inactive) with bifurcation and total.
     * The total is computed only from ACTIVE charges so it reflects
     * what residents are currently being billed.
     *
     * @param societyId  target society
     * @param adminEmail authenticated admin's email (for auth validation)
     */
    @Transactional(readOnly = true)
    public ChargeBreakdownResponse getChargeBreakdown(Long societyId, String adminEmail) {

        // 1. Validate admin belongs to this society
        Society society = resolveAdminSociety(societyId, adminEmail);

        // 2. Fetch ALL charges for display
        List<SocietyCharge> allCharges = societyChargeRepository.findBySocietyIdWithChargeType(societyId);

        // 3. Compute total from active charges only
        BigDecimal total = allCharges.stream()
                .filter(c -> isActive(c))
                .map(SocietyCharge::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Map to line items
        List<ChargeLineItem> lineItems = allCharges.stream()
                .map(this::toLineItem)
                .collect(Collectors.toList());

        return ChargeBreakdownResponse.builder()
                .societyId(society.getId())
                .societyName(society.getSocietyName())
                .totalMonthlyCharges(total)
                .charges(lineItems)
                .build();
    }

    // -------------------------------------------------------
    // CREATE — add a new charge type + society charge in one step
    // -------------------------------------------------------

    /**
     * Adds a new charge to a society.
     *
     * If a ChargeType with the given name already exists (case-insensitive),
     * it is reused rather than creating a duplicate. A new SocietyCharge row
     * is always created.
     *
     * The updated breakdown (including the new charge) is returned immediately.
     */
    @Transactional
    public ChargeBreakdownResponse addCharge(Long societyId, AddChargeRequest request, String adminEmail) {

        Society society = resolveAdminSociety(societyId, adminEmail);

        // Reuse or create the ChargeType
        ChargeType chargeType = chargeTypeRepository
                .findByNameIgnoreCase(request.getChargeTypeName())
                .orElseGet(() -> {
                    ChargeType newType = new ChargeType();
                    newType.setName(request.getChargeTypeName());
                    newType.setDescription(request.getChargeTypeDescription());
                    return chargeTypeRepository.save(newType);
                });

        // Create the SocietyCharge linking this society → chargeType
        SocietyCharge charge = new SocietyCharge();
        charge.setSociety(society);
        charge.setChargeType(chargeType);
        charge.setAmount(request.getAmount());
        charge.setCalculationType(request.getCalculationType());
        charge.setEffectiveFrom(
                request.getEffectiveFrom() != null ? request.getEffectiveFrom() : LocalDate.now()
        );
        charge.setEffectiveTo(request.getEffectiveTo()); // null = open-ended / currently active

        societyChargeRepository.save(charge);

        // Return the refreshed full breakdown
        return getChargeBreakdown(societyId, adminEmail);
    }

    // -------------------------------------------------------
    // UPDATE — edit billing parameters of an existing charge
    // -------------------------------------------------------

    /**
     * Updates the billing parameters (amount, calculationType, dates) of an
     * existing SocietyCharge. The ChargeType name/description is intentionally
     * NOT mutated here — use the admin data-management API for that.
     *
     * NOTE: Updating a charge does NOT retroactively alter issued invoices.
     * Changes apply only to invoices generated AFTER this update, which is
     * the correct accounting behaviour.
     */
    @Transactional
    public ChargeBreakdownResponse updateCharge(
            Long societyId,
            Long societyChargeId,
            UpdateChargeRequest request,
            String adminEmail) {

        resolveAdminSociety(societyId, adminEmail); // auth check

        SocietyCharge charge = societyChargeRepository.findById(societyChargeId)
                .orElseThrow(() -> new RuntimeException("SocietyCharge not found: " + societyChargeId));

        // Verify the charge actually belongs to the requested society
        if (!charge.getSociety().getId().equals(societyId)) {
            throw new RuntimeException("Security Exception: This charge does not belong to your society.");
        }

        if (request.getAmount() != null)          charge.setAmount(request.getAmount());
        if (request.getCalculationType() != null) charge.setCalculationType(request.getCalculationType());
        if (request.getEffectiveFrom() != null)   charge.setEffectiveFrom(request.getEffectiveFrom());
        // effectiveTo CAN be explicitly set to null (removing end date, making charge active again)
        charge.setEffectiveTo(request.getEffectiveTo());

        societyChargeRepository.save(charge);

        return getChargeBreakdown(societyId, adminEmail);
    }

    // -------------------------------------------------------
    // DELETE — remove a society charge
    // -------------------------------------------------------

    /**
     * Deletes a SocietyCharge. The ChargeType itself is NOT deleted —
     * it may be shared across other societies or referenced by invoice_line_items.
     *
     * Returns the updated breakdown after deletion.
     */
    @Transactional
    public ChargeBreakdownResponse deleteCharge(Long societyId, Long societyChargeId, String adminEmail) {

        resolveAdminSociety(societyId, adminEmail); // auth check

        SocietyCharge charge = societyChargeRepository.findById(societyChargeId)
                .orElseThrow(() -> new RuntimeException("SocietyCharge not found: " + societyChargeId));

        if (!charge.getSociety().getId().equals(societyId)) {
            throw new RuntimeException("Security Exception: This charge does not belong to your society.");
        }

        societyChargeRepository.delete(charge);

        return getChargeBreakdown(societyId, adminEmail);
    }

    // -------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------

    /**
     * Resolves the Society that the authenticated admin is authorised to manage.
     * Throws RuntimeException if the admin does not own a flat in the given society.
     */
    private Society resolveAdminSociety(Long societyId, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin user not found: " + adminEmail));

        return admin.getFlats().stream()
                .filter(flat -> flat.getSociety() != null
                        && flat.getSociety().getId().equals(societyId))
                .map(flat -> flat.getSociety())
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Security Exception: You do not manage society " + societyId));
    }

    private boolean isActive(SocietyCharge charge) {
        return charge.getEffectiveTo() == null
                || !charge.getEffectiveTo().isBefore(LocalDate.now());
    }

    private ChargeLineItem toLineItem(SocietyCharge charge) {
        return ChargeLineItem.builder()
                .societyChargeId(charge.getId())
                .chargeTypeId(charge.getChargeType().getId())
                .chargeTypeName(charge.getChargeType().getName())
                .chargeTypeDescription(charge.getChargeType().getDescription())
                .amount(charge.getAmount())
                .calculationType(charge.getCalculationType())
                .effectiveFrom(charge.getEffectiveFrom())
                .effectiveTo(charge.getEffectiveTo())
                .isActive(isActive(charge))
                .build();
    }
}