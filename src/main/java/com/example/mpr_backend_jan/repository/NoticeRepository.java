package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n WHERE n.society.id = :societyId AND n.expirationDate > :now ORDER BY n.createdAt DESC")
    List<Notice> findActiveNoticesForSociety(
            @Param("societyId") Long societyId,
            @Param("now") LocalDateTime now
    );

    // Add this right below your existing method
    @Query("SELECT n FROM Notice n WHERE n.society.id IN :societyIds AND n.expirationDate > :now ORDER BY n.createdAt DESC")
    List<Notice> findActiveNoticesForMultipleSocieties(
            @Param("societyIds") List<Long> societyIds,
            @Param("now") LocalDateTime now
    );
}