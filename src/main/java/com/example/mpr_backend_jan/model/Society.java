package com.example.mpr_backend_jan.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "society")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Society {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "society_name")
    private String societyName;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "primary_contact_name")
    private String primaryContactName;

    @Column(name = "primary_contact_phone")
    private String primaryContactPhone;

    @PrePersist
    public void onCreate() {
        LocalDateTime createdAt = LocalDateTime.now();
    }

}
