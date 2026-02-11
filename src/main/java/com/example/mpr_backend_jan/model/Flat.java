package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "flats")
@Getter
@Setter
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

}

