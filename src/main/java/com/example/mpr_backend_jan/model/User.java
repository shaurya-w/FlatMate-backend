package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // A single user can own multiple flats.
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Flat> flats;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}