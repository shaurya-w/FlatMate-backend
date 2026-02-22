
package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InvoiceResponse {
        private Long invoiceId;
        private Long flatId;

        private String name;
        private String email;
        private String flatNumber;

        private String societyName;

        private BigDecimal amount;
        private LocalDate billingMonth;
        private LocalDate dueDate;
}
