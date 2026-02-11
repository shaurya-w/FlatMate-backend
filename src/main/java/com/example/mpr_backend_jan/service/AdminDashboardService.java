package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.AdminTableRow;
import com.example.mpr_backend_jan.repository.AdminDashboardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    private final AdminDashboardRepository repo;

    public AdminDashboardService(AdminDashboardRepository repo) {
        this.repo = repo;
    }

    public List<AdminTableRow> getTable() {
        return repo.fetchAdminTable();
    }
}

