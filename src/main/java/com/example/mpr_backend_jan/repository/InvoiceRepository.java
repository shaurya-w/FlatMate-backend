package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.dto.InvoiceResponse;
import com.example.mpr_backend_jan.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
        // ------------------------------------------------------------------
        // QUERY 1 — Single invoice by ID
        //
        // Fetches:  Invoice + Flat + User + Society  (all ManyToOne hops)
        //           in one SQL JOIN.
        //
        // Line items are intentionally excluded here. Mixing a JOIN FETCH on
        // a @OneToMany (lineItems) with the ManyToOne joins above produces a
        // Cartesian product: every line-item row is duplicated for every join
        // branch. Instead, line items are loaded by Query 2 below using a
        // second targeted query — Hibernate batches these two round-trips far
        // more efficiently than one bloated multiplied result set.
        // ------------------------------------------------------------------
        @Query("""
            SELECT i FROM Invoice i
            JOIN FETCH i.flat f
            JOIN FETCH f.user u
            JOIN FETCH i.society s
            WHERE i.id = :id
            """)
        Optional<Invoice> findByIdWithFlatAndUser(@Param("id") Long id);


        // ------------------------------------------------------------------
        // QUERY 2 — Line items for a given invoice
        //
        // Fetches:  InvoiceLineItem + ChargeType  in one SQL JOIN.
        //
        // Called immediately after Query 1. Two small targeted queries are
        // always cheaper than one Cartesian-multiplied query for a
        // parent + @OneToMany relationship.
        // ------------------------------------------------------------------
        @Query("""
            SELECT i FROM Invoice i
            JOIN FETCH i.lineItems li
            JOIN FETCH li.chargeType
            WHERE i.id = :id
            """)
        Optional<Invoice> findByIdWithLineItems(@Param("id") Long id);


        // ------------------------------------------------------------------
        // QUERY 3 — All invoices for a flat (billing history view)
        //
        // Fetches:  Invoice + Flat + User + Society.
        // Line items are not included — the list view only needs summary
        // financials; line items are fetched on demand when a single invoice
        // is opened (Queries 1 + 2).
        // ------------------------------------------------------------------
        @Query("""
            SELECT i FROM Invoice i
            JOIN FETCH i.flat f
            JOIN FETCH f.user u
            JOIN FETCH i.society s
            WHERE f.id = :flatId
            ORDER BY i.billingMonth DESC
            """)
        List<Invoice> findAllByFlatId(@Param("flatId") Long flatId);


        // ------------------------------------------------------------------
        // QUERY 4 — All invoices for a society in a given billing month
        //
        // Used by the cron job to check for already-generated invoices before
        // running monthly invoice generation, preventing duplicates.
        // ------------------------------------------------------------------


        @Query("""
SELECT i FROM Invoice i
JOIN FETCH i.flat f
JOIN FETCH f.user u
JOIN FETCH i.society s
WHERE u.id IN :userIds
""")
        List<Invoice> findInvoicesByUserIds(@Param("userIds") List<Long> userIds);
}