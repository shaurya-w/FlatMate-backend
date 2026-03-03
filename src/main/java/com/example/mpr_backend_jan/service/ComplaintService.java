package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.ComplaintAdminUpdateRequest;
import com.example.mpr_backend_jan.dto.ComplaintRequest;
import com.example.mpr_backend_jan.dto.ComplaintResponse;
import com.example.mpr_backend_jan.model.Complaint;
import com.example.mpr_backend_jan.model.Flat;
import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.repository.ComplaintRepository;
import com.example.mpr_backend_jan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    @Transactional
    public ComplaintResponse createComplaint(ComplaintRequest request, String email) {
        User resident = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate that the user actually owns a flat in the requested society
        Flat targetFlat = resident.getFlats().stream()
                .filter(flat -> flat.getSociety() != null && flat.getSociety().getId().equals(request.getSocietyId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Security Exception: You do not own a flat in this society."));

        Complaint complaint = Complaint.builder()
                .subject(request.getSubject())
                .body(request.getBody())
                .tag(request.getTag())
                .resident(resident)
                .flat(targetFlat) // Automatically captures wing, floor, flat details
                .society(targetFlat.getSociety())
                .build();

        Complaint savedComplaint = complaintRepository.save(complaint);
        return mapToResponse(savedComplaint);
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponse> getMyComplaints(Long societyId, String email) {
        User resident = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return complaintRepository.findByResidentIdAndSocietyIdOrderByCreatedAtDesc(resident.getId(), societyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponse> getSocietyComplaintsForAdmin(Long societyId, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();

        // Ensure this admin actually belongs to this society
        boolean isAdminOfSociety = admin.getFlats().stream()
                .anyMatch(f -> f.getSociety() != null && f.getSociety().getId().equals(societyId));

        if (!isAdminOfSociety) {
            throw new RuntimeException("Unauthorized: You do not manage this society.");
        }

        return complaintRepository.findBySocietyIdOrderByCreatedAtDesc(societyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ComplaintResponse updateComplaintStatus(Long complaintId, ComplaintAdminUpdateRequest request, String adminEmail) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        // Validate admin has rights to this complaint's society
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();
        boolean isAdminOfSociety = admin.getFlats().stream()
                .anyMatch(f -> f.getSociety() != null && f.getSociety().getId().equals(complaint.getSociety().getId()));

        if (!isAdminOfSociety) {
            throw new RuntimeException("Unauthorized: Cannot update complaints outside your society.");
        }

        complaint.setStatus(request.getStatus());
        if (request.getAdminMessage() != null) {
            complaint.setAdminMessage(request.getAdminMessage());
        }

        return mapToResponse(complaintRepository.save(complaint));
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {
        return ComplaintResponse.builder()
                .id(complaint.getId())
                .complaintNumber(complaint.getComplaintNumber())
                .subject(complaint.getSubject())
                .body(complaint.getBody())
                .status(complaint.getStatus())
                .tag(complaint.getTag())
                .adminMessage(complaint.getAdminMessage())
                .createdAt(complaint.getCreatedAt())
                .residentName(complaint.getResident().getName())
                .flatNumber(complaint.getFlat().getFlatNumber())
                .wing(complaint.getFlat().getWing())
                .societyName(complaint.getSociety().getSocietyName())
                .build();
    }
}