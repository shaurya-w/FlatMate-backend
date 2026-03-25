package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.ContactRequest;
import com.example.mpr_backend_jan.dto.ContactResponse;
import com.example.mpr_backend_jan.service.SocietyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
public class SocietyContactController {

    private final SocietyContactService contactService;

    // -------------------------------------------------------
    // GET  /api/admin/contacts/society/{societyId}
    // List all contacts for a society
    // -------------------------------------------------------
    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<ContactResponse>> getContacts(
            @PathVariable Long societyId,
            Authentication authentication) {

        return ResponseEntity.ok(
                contactService.getContacts(societyId, authentication.getName())
        );
    }

    // -------------------------------------------------------
    // POST /api/admin/contacts
    // Add a new contact — societyId resolved from session.
    //
    // If the admin belongs to only ONE society (the common case),
    // no extra parameter is needed at all.
    //
    // If the admin manages multiple societies, pass the target as
    // an optional query param:
    //   POST /api/admin/contacts?societyId=2
    // -------------------------------------------------------
    @PostMapping
    public ResponseEntity<ContactResponse> addContact(
            @RequestBody ContactRequest request,
            @RequestParam(required = false) Long societyId,
            Authentication authentication) {

        return ResponseEntity.ok(
                contactService.addContact(request, authentication.getName(), societyId)
        );
    }

    // -------------------------------------------------------
    // PUT  /api/admin/contacts/{contactId}
    // Update an existing contact
    // -------------------------------------------------------
    @PutMapping("/{contactId}")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long contactId,
            @RequestBody ContactRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                contactService.updateContact(contactId, request, authentication.getName())
        );
    }

    // -------------------------------------------------------
    // DELETE /api/admin/contacts/{contactId}
    // Delete a contact
    // -------------------------------------------------------
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(
            @PathVariable Long contactId,
            Authentication authentication) {

        contactService.deleteContact(contactId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}