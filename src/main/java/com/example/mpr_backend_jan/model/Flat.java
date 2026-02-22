package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "flats")
@Getter
@Setter
@NoArgsConstructor
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flat_number")
    private String flatNumber;

    private String wing;

    @ManyToOne
    @JoinColumn(name = "society_id")
    private Society society;

    // One-to-one user relationship (Flat owns FK)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // One-to-many invoices
    @OneToMany(mappedBy = "flat")
    private List<Invoice> invoices;
}