package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.dto.InvoiceResponse;
import com.example.mpr_backend_jan.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

        // Fetch DTO directly using mapped associations
        @Query("""
        SELECT new com.example.mpr_backend_jan.dto.InvoiceResponse(
            i.id,
            f.id,
            u.name,
            u.email,
            f.flatNumber,
            s.societyName,
            i.amount,
            i.billingMonth,
            i.dueDate
        )
        FROM Invoice i
        JOIN i.flat f
        JOIN f.society s
        LEFT JOIN f.user u
        WHERE i.id IN :ids
    """)
        List<InvoiceResponse> findInvoicesByIds(@Param("ids") List<Long> ids);

        // Optional: fetch raw Invoice entities if needed
        List<Invoice> findByIdIn(List<Long> invoiceIds);
}