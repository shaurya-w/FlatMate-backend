package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.ContactRequest;
import com.example.mpr_backend_jan.dto.ContactResponse;
import com.example.mpr_backend_jan.model.Flat;
import com.example.mpr_backend_jan.model.Society;
import com.example.mpr_backend_jan.model.SocietyContact;
import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.repository.SocietyContactRepository;
import com.example.mpr_backend_jan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocietyContactService {

    private final SocietyContactRepository contactRepository;
    private final UserRepository userRepository;

    // -------------------------------------------------------
    // READ — all contacts for a society
    // -------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ContactResponse> getContacts(Long societyId, String adminEmail) {
        resolveAdminSociety(societyId, adminEmail); // auth check

        return contactRepository.findBySocietyId(societyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // CREATE — societyId resolved implicitly from session
    // -------------------------------------------------------

    /**
     * If societyId is provided (admin manages multiple societies), use it.
     * If null, resolve automatically from the admin's single society.
     * Throws if the admin manages 0 or multiple societies and none is specified.
     */
    @Transactional
    public ContactResponse addContact(ContactRequest request, String adminEmail, Long societyId) {
        Society society = resolveSociety(adminEmail, societyId);

        SocietyContact contact = SocietyContact.builder()
                .society(society)
                .name(request.getName())
                .purpose(request.getPurpose())
                .email(request.getEmail())
                .phoneNumbers(request.getPhoneNumbers())
                .build();

        return toResponse(contactRepository.save(contact));
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------

    @Transactional
    public ContactResponse updateContact(Long contactId, ContactRequest request, String adminEmail) {

        SocietyContact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found: " + contactId));

        // Verify the admin has rights to the society this contact belongs to
        resolveAdminSociety(contact.getSociety().getId(), adminEmail);

        if (request.getName() != null)         contact.setName(request.getName());
        if (request.getPurpose() != null)      contact.setPurpose(request.getPurpose());
        if (request.getEmail() != null)        contact.setEmail(request.getEmail());
        if (request.getPhoneNumbers() != null) contact.setPhoneNumbers(request.getPhoneNumbers());

        return toResponse(contactRepository.save(contact));
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------

    @Transactional
    public void deleteContact(Long contactId, String adminEmail) {

        SocietyContact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found: " + contactId));

        resolveAdminSociety(contact.getSociety().getId(), adminEmail);
        contactRepository.delete(contact);
    }

    // -------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------

    /**
     * Primary resolver: called when the societyId is already known
     * (GET list, UPDATE, DELETE). Validates admin owns that society.
     */
    private Society resolveAdminSociety(Long societyId, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + adminEmail));

        return admin.getFlats().stream()
                .filter(f -> f.getSociety() != null && f.getSociety().getId().equals(societyId))
                .map(Flat::getSociety)
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Security Exception: You do not manage society " + societyId));
    }

    /**
     * Implicit resolver: called on CREATE when societyId may not be provided.
     *
     * Rules:
     *   - If societyId is explicitly passed (admin manages multiple), validate and use it.
     *   - If null and admin has exactly 1 society, use that automatically.
     *   - If null and admin has multiple societies, throw a clear error asking them to specify.
     *   - If admin has no societies, throw.
     */
    private Society resolveSociety(String adminEmail, Long societyId) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + adminEmail));

        List<Society> adminSocieties = admin.getFlats().stream()
                .filter(f -> f.getSociety() != null)
                .map(Flat::getSociety)
                .distinct()
                .collect(Collectors.toList());

        if (adminSocieties.isEmpty()) {
            throw new RuntimeException("Admin is not associated with any society.");
        }

        // societyId explicitly provided — validate and use it
        if (societyId != null) {
            return adminSocieties.stream()
                    .filter(s -> s.getId().equals(societyId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Security Exception: You do not manage society " + societyId));
        }

        // No societyId provided — auto-resolve if unambiguous
        if (adminSocieties.size() == 1) {
            return adminSocieties.get(0);
        }

        // Multiple societies — admin must specify
        List<Long> ids = adminSocieties.stream().map(Society::getId).collect(Collectors.toList());
        throw new RuntimeException(
                "You manage multiple societies " + ids + ". Please pass ?societyId= to specify which one.");
    }

    @Transactional(readOnly = true)
    public List<ContactResponse> getContactsForResident(Long societyId, String residentEmail) {
        User resident = userRepository.findByEmail(residentEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + residentEmail));

        // Security check: resident must own a flat in the requested society
        boolean belongsToSociety = resident.getFlats().stream()
                .anyMatch(f -> f.getSociety() != null && f.getSociety().getId().equals(societyId));

        if (!belongsToSociety) {
            throw new RuntimeException("Security Exception: You do not belong to society " + societyId);
        }

        return contactRepository.findBySocietyId(societyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ContactResponse toResponse(SocietyContact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .purpose(contact.getPurpose())
                .email(contact.getEmail())
                .phoneNumbers(contact.getPhoneNumbers())
                .societyId(contact.getSociety().getId())
                // societyName intentionally excluded
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}