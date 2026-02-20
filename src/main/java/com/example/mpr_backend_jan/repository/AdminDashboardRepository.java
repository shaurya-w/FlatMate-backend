package com.example.mpr_backend_jan.repository;
import com.example.mpr_backend_jan.dto.AdminTableRow;
import com.example.mpr_backend_jan.model.Flat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDashboardRepository extends JpaRepository<Flat, Long> {

    @Query("""
    SELECT new com.example.mpr_backend_jan.dto.AdminTableRow(
        f.id,
        f.flatNumber,
        f.wing,
        u.name,
        u.phone,
        u.email,
        COALESCE(SUM(
            CASE WHEN i.status IN ('ISSUED','OVERDUE') THEN i.amount END
        ),0),
        CASE
            WHEN SUM(CASE WHEN i.status = 'OVERDUE' THEN 1 ELSE 0 END) > 0 THEN 'OVERDUE'
            WHEN SUM(CASE WHEN i.status = 'ISSUED' THEN 1 ELSE 0 END) > 0 THEN 'PENDING'
            ELSE 'PAID'
        END
    )
    FROM Flat f
    JOIN FlatUser fu ON fu.flat = f AND fu.endDate IS NULL
    JOIN User u ON fu.user = u
    LEFT JOIN Invoice i ON i.flat = f
    GROUP BY f.id, f.flatNumber, f.wing, u.name, u.phone, u.email
    """)
    List<AdminTableRow> fetchAdminTable();
}

