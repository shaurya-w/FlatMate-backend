package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "society_charges")
@NoArgsConstructor
public class SocietyCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many society charges can belong to one society
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    // Many society charges can reference one charge type
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_type_id", nullable = false)
    private ChargeType chargeType;

    private BigDecimal amount;

    private String calculationType;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

}
