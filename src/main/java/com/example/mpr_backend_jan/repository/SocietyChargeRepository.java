package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.SocietyCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SocietyChargeRepository extends JpaRepository<SocietyCharge, Long> {

    @Query("""
            SELECT sc FROM SocietyCharge sc
            JOIN FETCH sc.chargeType ct
            WHERE sc.society.id = :societyId
            ORDER BY sc.effectiveFrom DESC
            """)
    List<SocietyCharge> findBySocietyIdWithChargeType(@Param("societyId") Long societyId);

    @Query("""
            SELECT sc FROM SocietyCharge sc
            JOIN FETCH sc.chargeType ct
            WHERE sc.society.id = :societyId
              AND (sc.effectiveTo IS NULL OR sc.effectiveTo >= CURRENT_DATE)
            ORDER BY ct.name ASC
            """)
    List<SocietyCharge> findActiveChargesBySocietyId(@Param("societyId") Long societyId);
}