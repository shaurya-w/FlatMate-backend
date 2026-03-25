package com.example.mpr_backend_jan.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Used when an Admin edits an EXISTING SocietyCharge row.
 * The chargeType itself (name/description) is NOT changed here —
 * only the billing parameters for this society's charge entry.
 */
@Getter
@Setter
public class UpdateChargeRequest {

    private BigDecimal amount;
    private String calculationType;  // "FIXED" | "PER_SQFT" | "PER_UNIT"
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;   // nullable
}