package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.AddChargeRequest;
import com.example.mpr_backend_jan.dto.ChargeBreakdownResponse;
import com.example.mpr_backend_jan.dto.UpdateChargeRequest;
import com.example.mpr_backend_jan.service.ChargeManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * All endpoints are under /api/admin/** and are therefore protected
 * by SecurityConfig to ROLE_ADMIN only.
 */
@RestController
@RequestMapping("/api/admin/charges")
@RequiredArgsConstructor
public class ChargeManagementController {

    private final ChargeManagementService chargeManagementService;

    // -------------------------------------------------------
    // GET  /api/admin/charges/society/{societyId}
    // Returns full bifurcation + total for the society
    // -------------------------------------------------------
    @GetMapping("/society/{societyId}")
    public ResponseEntity<ChargeBreakdownResponse> getChargeBreakdown(
            @PathVariable Long societyId,
            Authentication authentication) {

        return ResponseEntity.ok(
                chargeManagementService.getChargeBreakdown(societyId, authentication.getName())
        );
    }

    // -------------------------------------------------------
    // POST /api/admin/charges/society/{societyId}
    // Add a new charge type + society charge in one request
    // -------------------------------------------------------
    @PostMapping("/society/{societyId}")
    public ResponseEntity<ChargeBreakdownResponse> addCharge(
            @PathVariable Long societyId,
            @RequestBody AddChargeRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                chargeManagementService.addCharge(societyId, request, authentication.getName())
        );
    }

    // -------------------------------------------------------
    // PUT  /api/admin/charges/society/{societyId}/{societyChargeId}
    // Edit billing parameters of an existing SocietyCharge
    // -------------------------------------------------------
    @PutMapping("/society/{societyId}/{societyChargeId}")
    public ResponseEntity<ChargeBreakdownResponse> updateCharge(
            @PathVariable Long societyId,
            @PathVariable Long societyChargeId,
            @RequestBody UpdateChargeRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                chargeManagementService.updateCharge(
                        societyId, societyChargeId, request, authentication.getName()
                )
        );
    }

    // -------------------------------------------------------
    // DELETE /api/admin/charges/society/{societyId}/{societyChargeId}
    // Remove a SocietyCharge (ChargeType record is preserved)
    // -------------------------------------------------------
    @DeleteMapping("/society/{societyId}/{societyChargeId}")
    public ResponseEntity<ChargeBreakdownResponse> deleteCharge(
            @PathVariable Long societyId,
            @PathVariable Long societyChargeId,
            Authentication authentication) {

        return ResponseEntity.ok(
                chargeManagementService.deleteCharge(
                        societyId, societyChargeId, authentication.getName()
                )
        );
    }
}