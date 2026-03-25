package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.SocietyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SocietyContactRepository extends JpaRepository<SocietyContact, Long> {

    /**
     * Fetch all contacts for a given society, ordered by name.
     * Eagerly loads phoneNumbers via JOIN FETCH to avoid N+1
     * (phoneNumbers is @ElementCollection, not a standard relation).
     */
    @Query("""
            SELECT DISTINCT sc FROM SocietyContact sc
            LEFT JOIN FETCH sc.phoneNumbers
            WHERE sc.society.id = :societyId
            ORDER BY sc.name ASC
            """)
    List<SocietyContact> findBySocietyId(@Param("societyId") Long societyId);
}