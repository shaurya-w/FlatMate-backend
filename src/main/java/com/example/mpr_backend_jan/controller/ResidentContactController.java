package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.ContactResponse;
import com.example.mpr_backend_jan.service.SocietyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResidentContactController {

    private final SocietyContactService contactService;

    @GetMapping("/api/contacts/society/{societyId}")
    public ResponseEntity<List<ContactResponse>> getContactsForResident(
            @PathVariable Long societyId,
            Authentication authentication) {

        return ResponseEntity.ok(
                contactService.getContactsForResident(societyId, authentication.getName())
        );
    }
}