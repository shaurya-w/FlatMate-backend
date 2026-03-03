package com.example.mpr_backend_jan.dto;

import com.example.mpr_backend_jan.model.ComplaintTag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintRequest {
    private String subject;
    private String body;
    private Long societyId; // Allows multi-society users to choose where to post
    private ComplaintTag tag;
}