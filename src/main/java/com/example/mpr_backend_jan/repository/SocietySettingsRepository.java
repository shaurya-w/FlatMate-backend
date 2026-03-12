package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.SocietySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SocietySettingsRepository extends JpaRepository<SocietySettings, Long> {

    // ------------------------------------------------------------------
    // SocietySettings owns the FK to Society (society_id column lives on
    // the society_settings table). There is no back-reference field on
    // Society itself, so this cannot be traversed from an Invoice in JPQL.
    // This repository is the only correct way to load branding data.
    //
    // Returns Optional.empty() if the society has not yet configured
    // settings — callers must handle this gracefully (InvoiceResponse
    // already null-checks the settings parameter).
    // ------------------------------------------------------------------
    @Query("""
            SELECT ss FROM SocietySettings ss
            WHERE ss.society.id = :societyId
            """)
    Optional<SocietySettings> findBySocietyId(@Param("societyId") Long societyId);

}