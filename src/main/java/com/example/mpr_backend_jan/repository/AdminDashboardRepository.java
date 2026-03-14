package com.example.mpr_backend_jan.repository;

import com.example.mpr_backend_jan.dto.AdminTableRow;
import com.example.mpr_backend_jan.model.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminDashboardRepository extends JpaRepository<Flat, Long> {

    @Query("SELECT new com.example.mpr_backend_jan.dto.AdminTableRow(" +
            "f.id, f.flatNumber, f.wing, u.id, u.name, u.phone, u.email, " +
            "CAST(COALESCE(SUM(CASE WHEN i.status IN (" +
            "com.example.mpr_backend_jan.model.InvoiceStatus.ISSUED, " +
            "com.example.mpr_backend_jan.model.InvoiceStatus.OVERDUE) " +
            "THEN i.totalAmount ELSE 0 END), 0) AS BigDecimal), " +
            "CASE WHEN SUM(CASE WHEN i.status = com.example.mpr_backend_jan.model.InvoiceStatus.OVERDUE THEN 1 ELSE 0 END) > 0 THEN 'OVERDUE' " +
            "     WHEN SUM(CASE WHEN i.status = com.example.mpr_backend_jan.model.InvoiceStatus.ISSUED THEN 1 ELSE 0 END) > 0 THEN 'PENDING' " +
            "     ELSE 'PAID' END) " +
            "FROM Flat f " +
            "LEFT JOIN f.user u " +
            "LEFT JOIN f.invoices i " +
            "WHERE u.role = 'USER' " + // <-- added filter for role
            "GROUP BY f.id, f.flatNumber, f.wing, u.id, u.name, u.phone, u.email")
    List<AdminTableRow> fetchAdminTable();

}