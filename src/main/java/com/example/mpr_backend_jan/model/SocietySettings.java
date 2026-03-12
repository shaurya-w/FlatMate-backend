package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "society_settings")
public class SocietySettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One society has one settings record
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false, unique = true)
    private Society society;

    private String logoUrl;

    private String stampImageUrl;

    private String authorizedSignatoryName;

    private String authorizedSignatoryRole;

    @Column(columnDefinition = "TEXT")
    private String invoiceFooterText;

}
