package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.AdminTableRow;
import com.example.mpr_backend_jan.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    protected final AdminDashboardService service;

    @GetMapping("/dashboard")
    public List<AdminTableRow> getTable() {
        return service.getTable();
    }
}

