package com.example.mpr_backend_jan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Request body for creating or updating a SocietyContact.
 *
 * societyId is intentionally NOT included here — it is resolved
 * server-side from the authenticated admin's session. If the admin
 * belongs to exactly one society, it is used automatically. If they
 * belong to multiple, they must pass ?societyId= as a query param.
 */
@Getter
@Setter
public class ContactRequest {
    private String name;
    private String purpose;
    private String email;
    private List<String> phoneNumbers;
}