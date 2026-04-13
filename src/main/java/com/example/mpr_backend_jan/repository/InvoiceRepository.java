package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

        @Query("""
    SELECT i FROM Invoice i
    JOIN FETCH i.flat f
    JOIN FETCH f.user u
    JOIN FETCH i.society s
    WHERE u.id = :userId
    ORDER BY i.billingMonth DESC
    """)
        List<Invoice> findAllByUserId(@Param("userId") Long userId);

        @Query("""
    SELECT i FROM Invoice i
    JOIN FETCH i.flat f
    JOIN FETCH f.user u
    JOIN FETCH i.society s
    WHERE i.id = :id
    """)
        Optional<Invoice> findByIdWithFlatAndUser(@Param("id") Long id);

        @Query("""
    SELECT DISTINCT i FROM Invoice i
    LEFT JOIN FETCH i.lineItems li
    LEFT JOIN FETCH li.chargeType
    WHERE i.id = :id
    """)
        Optional<Invoice> findByIdWithLineItems(@Param("id") Long id);

        @Query("""
    SELECT i FROM Invoice i
    JOIN FETCH i.flat f
    JOIN FETCH f.user u
    JOIN FETCH i.society s
    WHERE f.id = :flatId
    ORDER BY i.billingMonth DESC
    """)
        List<Invoice> findAllByFlatId(@Param("flatId") Long flatId);

        @Query("""
    SELECT i FROM Invoice i
    JOIN FETCH i.flat f
    JOIN FETCH f.user u
    JOIN FETCH i.society s
    WHERE u.id IN :userIds
    AND i.status = com.example.mpr_backend_jan.model.InvoiceStatus.ISSUED
    """)
        List<Invoice> findUnpaidInvoicesByUserIds(@Param("userIds") List<Long> userIds);
}