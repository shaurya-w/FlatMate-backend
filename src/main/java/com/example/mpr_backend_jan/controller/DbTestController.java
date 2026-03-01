package com.example.mpr_backend_jan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class DbTestController {

    private final JdbcTemplate jdbcTemplate;

    // Spring automatically injects JdbcTemplate which uses your configured datasource
    public DbTestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/ping-db")
    public ResponseEntity<String> pingDatabase() {
        try {
            // Execute a lightweight query to test the connection
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                return ResponseEntity.ok("Database connection is successful! Database returned: " + result);
            } else {
                return ResponseEntity.status(500).body("Query executed but returned unexpected result.");
            }
        } catch (Exception e) {
            // If the connection fails, it will throw an exception here
            return ResponseEntity.status(500).body("Database connection failed: " + e.getMessage());
        }
    }
}