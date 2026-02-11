package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    @ManyToOne
    @JoinColumn(name = "flat_id")
    private Flat flat;

    @Column(name = "billing_month")
    private LocalDate billingMonth;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}

