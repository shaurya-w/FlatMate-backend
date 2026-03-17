package com.example.mpr_backend_jan.dto;

import com.example.mpr_backend_jan.model.InvoiceStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class InvoiceSummaryDTO {

    private final Long invoiceId;
    private final String flatNumber;
    private final String wing;

    private final BigDecimal totalAmount;
    private final BigDecimal amountPaid;
    private final BigDecimal pendingAmount;

    private final InvoiceStatus status;

    private final LocalDate billingMonth;
    private final LocalDate dueDate;

    // Constructor used by JPQL
    public InvoiceSummaryDTO(
            Long invoiceId,
            String flatNumber,
            String wing,
            BigDecimal totalAmount,
            BigDecimal amountPaid,
            InvoiceStatus status,
            LocalDate billingMonth,
            LocalDate dueDate
    ) {
        this.invoiceId = invoiceId;
        this.flatNumber = flatNumber;
        this.wing = wing;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.pendingAmount = totalAmount.subtract(
                amountPaid != null ? amountPaid : BigDecimal.ZERO
        );
        this.status = status;
        this.billingMonth = billingMonth;
        this.dueDate = dueDate;
    }

    // Getters

}