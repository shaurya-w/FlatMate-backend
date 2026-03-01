package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.Flat;
import com.example.mpr_backend_jan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    // Spring Data JPA will automatically implement this query based on the method name
    Optional<Flat> findByUser(User user);
}