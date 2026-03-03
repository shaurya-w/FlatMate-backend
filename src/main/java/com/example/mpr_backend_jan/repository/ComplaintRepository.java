package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // For a resident to view their own history in a specific society
    List<Complaint> findByResidentIdAndSocietyIdOrderByCreatedAtDesc(Long residentId, Long societyId);

    // For an admin to view all complaints in their society
    List<Complaint> findBySocietyIdOrderByCreatedAtDesc(Long societyId);
}