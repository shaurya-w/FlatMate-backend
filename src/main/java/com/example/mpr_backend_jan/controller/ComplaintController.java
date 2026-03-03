package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.ComplaintAdminUpdateRequest;
import com.example.mpr_backend_jan.dto.ComplaintRequest;
import com.example.mpr_backend_jan.dto.ComplaintResponse;
import com.example.mpr_backend_jan.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    // --- RESIDENT ENDPOINTS ---

    @PostMapping("/api/complaints")
    public ResponseEntity<ComplaintResponse> createComplaint(
            @RequestBody ComplaintRequest request,
            Authentication auth) {
        ComplaintResponse response = complaintService.createComplaint(request, auth.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/complaints/society/{societyId}")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(
            @PathVariable Long societyId,
            Authentication auth) {
        return ResponseEntity.ok(complaintService.getMyComplaints(societyId, auth.getName()));
    }

    // --- ADMIN ENDPOINTS (Protected by SecurityConfig) ---

    @GetMapping("/api/admin/complaints/society/{societyId}")
    public ResponseEntity<List<ComplaintResponse>> getSocietyComplaints(
            @PathVariable Long societyId,
            Authentication auth) {
        return ResponseEntity.ok(complaintService.getSocietyComplaintsForAdmin(societyId, auth.getName()));
    }

    @PatchMapping("/api/admin/complaints/{complaintId}")
    public ResponseEntity<ComplaintResponse> updateComplaint(
            @PathVariable Long complaintId,
            @RequestBody ComplaintAdminUpdateRequest request,
            Authentication auth) {
        return ResponseEntity.ok(complaintService.updateComplaintStatus(complaintId, request, auth.getName()));
    }
}