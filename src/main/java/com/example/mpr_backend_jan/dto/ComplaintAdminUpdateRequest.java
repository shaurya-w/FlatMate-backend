package com.example.mpr_backend_jan.dto;

import com.example.mpr_backend_jan.model.ComplaintStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintAdminUpdateRequest {
    private ComplaintStatus status;
    private String adminMessage;
}