package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ContactResponse {
    private Long id;
    private String name;
    private String purpose;
    private String email;
    private List<String> phoneNumbers;
    private Long societyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
