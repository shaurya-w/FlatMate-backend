package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


//DTO should match the admin dashboard repo structure !

@Getter
@Setter
@AllArgsConstructor
public class AdminTableRow {

    private Long id;              // optional can remove later if not needed

    private String flatNumber;
    private String wing;
    private String residentName;
    private String phone;
    private String email;
    private BigDecimal totalDues;
    private String status;

}
