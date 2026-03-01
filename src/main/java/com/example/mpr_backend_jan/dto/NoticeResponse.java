package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;
}