package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.NoticeRequest;
import com.example.mpr_backend_jan.dto.NoticeResponse;
import com.example.mpr_backend_jan.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // Protected by SecurityConfig: only Admins can access /api/admin/**
    @PostMapping("/api/admin/notices")
    public ResponseEntity<NoticeResponse> createNotice(
            @RequestBody NoticeRequest request,
            Authentication authentication) {

        // Securely identify the author via the active session
        String authorEmail = authentication.getName();

        NoticeResponse response = noticeService.createNotice(request, authorEmail);
        return ResponseEntity.ok(response);
    }

    // Accessible by standard authenticated users to view the board
    @GetMapping("/api/notices/society/{societyId}")
    public ResponseEntity<List<NoticeResponse>> getActiveNotices(@PathVariable Long societyId) {
        List<NoticeResponse> activeNotices = noticeService.getActiveNotices(societyId);
        return ResponseEntity.ok(activeNotices);
    }

    // A smart, zero-parameter endpoint for residents to get their personal feed
    @GetMapping("/api/notices/my-feed")
    public ResponseEntity<List<NoticeResponse>> getMyFeed(Authentication authentication) {
        // Extract the resident's email securely from the session
        String userEmail = authentication.getName();

        List<NoticeResponse> personalizedFeed = noticeService.getMyNoticeFeed(userEmail);
        return ResponseEntity.ok(personalizedFeed);
    }
}