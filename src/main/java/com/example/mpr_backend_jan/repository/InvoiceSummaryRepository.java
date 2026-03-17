package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.dto.InvoiceSummaryDTO;
import com.example.mpr_backend_jan.model.Invoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceSummaryRepository extends JpaRepository<Invoice, Long> {

    @Query("""
        SELECT new com.example.mpr_backend_jan.dto.InvoiceSummaryDTO(
            i.id,
            f.flatNumber,
            f.wing,
            i.totalAmount,
            i.amountPaid,
            i.status,
            i.billingMonth,
            i.dueDate
        )
        FROM Invoice i
        JOIN i.flat f
        WHERE f.user.id = :userId
          AND i.status IN ('ISSUED', 'OVERDUE')
        ORDER BY 
            CASE WHEN i.status = 'OVERDUE' THEN 0 ELSE 1 END,
            i.dueDate ASC
    """)
    List<InvoiceSummaryDTO> findPendingInvoicesByUser(
            @Param("userId") Long userId,
            Pageable pageable
    );
}