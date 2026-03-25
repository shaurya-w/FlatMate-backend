package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "society_contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocietyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many contacts can belong to one society
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    @Column(nullable = false)
    private String name;

    // e.g. "Plumber", "Electrician", "Security Head", "Society Secretary"
    @Column(nullable = false)
    private String purpose;

    private String email;

    // Stored as a comma-separated list to allow multiple numbers
    // e.g. "9876543210,9123456789"
    // Exposed as List<String> via @ElementCollection to keep it clean
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "society_contact_phones",
            joinColumns = @JoinColumn(name = "contact_id")
    )
    @Column(name = "phone_number")
    private List<String> phoneNumbers;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}