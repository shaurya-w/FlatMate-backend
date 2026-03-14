package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByRazorpayPaymentId(String razorpayPaymentId);

}