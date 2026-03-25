package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Returned by GET /api/admin/charges/society/{societyId}
 *
 * Contains:
 *  - A line-by-line bifurcation of every active charge
 *  - A computed total of all active charge amounts
 */
@Getter
@Builder
@AllArgsConstructor
public class ChargeBreakdownResponse {

    private Long societyId;
    private String societyName;

    /** Sum of all active SocietyCharge amounts for this society. */
    private BigDecimal totalMonthlyCharges;

    /** Individual charge entries — one per SocietyCharge row. */
    private List<ChargeLineItem> charges;

    // -------------------------------------------------------
    // Inner class — one entry per SocietyCharge
    // -------------------------------------------------------
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChargeLineItem {
        private Long societyChargeId;       // SocietyCharge PK — used for edit/delete
        private Long chargeTypeId;
        private String chargeTypeName;
        private String chargeTypeDescription;
        private BigDecimal amount;
        private String calculationType;
        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;      // null = currently active
        private boolean isActive;           // effectiveTo == null || effectiveTo >= today
    }
}