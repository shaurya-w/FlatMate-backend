package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "razorpay_webhooks")
public class RazorpayWebhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventId;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String signature;

    private String status;

    private LocalDateTime receivedAt;

    private LocalDateTime processedAt;

    // getters setters
}