package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AdminTableRow {

    private String flatNumber;
    private String wing;
    private String residentName;
    private String phone;
    private String email;
    private BigDecimal totalDues;
    private String status;

//    public AdminTableRow(Long flatId, String flatNumber, String wing,
//                            String residentName, String phone, String email,
//                            BigDecimal totalDues, String status) {
//        this.flatNumber = flatNumber;
//        this.wing = wing;
//        this.residentName = residentName;
//        this.phone = phone;
//        this.email = email;
//        this.totalDues = totalDues;
//        this.status = status;
//    }
}
