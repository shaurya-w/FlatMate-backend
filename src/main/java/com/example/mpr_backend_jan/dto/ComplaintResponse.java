package com.example.mpr_backend_jan.dto;

import com.example.mpr_backend_jan.model.ComplaintStatus;
import com.example.mpr_backend_jan.model.ComplaintTag;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ComplaintResponse {
    private Long id;
    private String complaintNumber;
    private String subject;
    private String body;
    private ComplaintStatus status;
    private ComplaintTag tag;
    private String adminMessage;
    private LocalDateTime createdAt;

    // Auto-included resident details
    private String residentName;
    private String flatNumber;
    private String wing;
    private String societyName;
}
