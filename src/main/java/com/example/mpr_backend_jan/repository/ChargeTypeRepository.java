package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.ChargeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChargeTypeRepository extends JpaRepository<ChargeType, Long> {

    /**
     * Case-insensitive lookup by name so admins can't accidentally create
     * duplicate charge types like "electricity" and "Electricity".
     */
    Optional<ChargeType> findByNameIgnoreCase(String name);
}