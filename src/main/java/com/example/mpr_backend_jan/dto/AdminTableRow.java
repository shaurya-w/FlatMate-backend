package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AdminTableRow {

    private Long flatId;
    private String flatNumber;
    private String wing;

    private Long userId;
    private String name;
    private String phone;
    private String email;

    private BigDecimal totalPendingAmount;
    private String status;
}