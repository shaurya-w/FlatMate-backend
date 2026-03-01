package com.example.mpr_backend_jan.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Automatically creates a linked table for tags
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "notice_tags", joinColumns = @JoinColumn(name = "notice_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}