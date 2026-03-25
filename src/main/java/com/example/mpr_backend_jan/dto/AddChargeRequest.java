package com.example.mpr_backend_jan.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Used when an Admin wants to ADD a brand-new charge type
 * AND simultaneously attach it to their society as a SocietyCharge.
 *
 * Flow:
 *   1. A new ChargeType row is created (or an existing one is reused by name).
 *   2. A new SocietyCharge row is created linking the society → chargeType.
 */
@Getter
@Setter
public class AddChargeRequest {

    // --- ChargeType fields ---
    private String chargeTypeName;        // e.g. "Elevator Maintenance"
    private String chargeTypeDescription; // optional description

    // --- SocietyCharge fields ---
    private BigDecimal amount;
    private String calculationType;  // "FIXED" | "PER_SQFT" | "PER_UNIT"
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;   // nullable — null means "currently active"

    // Society is resolved server-side from the authenticated admin's session
}