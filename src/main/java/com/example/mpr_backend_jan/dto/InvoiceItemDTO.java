package com.example.mpr_backend_jan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {

    // The human-readable label, e.g. "Electricity Charges"
    private String description;

    // The rupee amount for this line item
    private BigDecimal amount;

    // -------------------------------------------------------
    // Static factory — maps directly from InvoiceLineItem
    // -------------------------------------------------------
    public static InvoiceItemDTO from(com.example.mpr_backend_jan.model.InvoiceLineItem item) {
        return InvoiceItemDTO.builder()
                .description(item.getDescription())
                .amount(item.getAmount())
                .build();
    }
}