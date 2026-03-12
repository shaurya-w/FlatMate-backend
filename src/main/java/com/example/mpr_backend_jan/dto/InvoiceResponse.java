package com.example.mpr_backend_jan.dto;

import com.example.mpr_backend_jan.model.Invoice;
import com.example.mpr_backend_jan.model.InvoiceStatus;
import com.example.mpr_backend_jan.model.SocietySettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {


// -------------------------------------------------------
// Identity
// -------------------------------------------------------
        private Long invoiceId;
        private String flatNumber;
        private String wing;
        private String name;
        private String email;
        private LocalDate billingMonth;
        private LocalDate dueDate;

        // -------------------------------------------------------
        // Metadata
        // -------------------------------------------------------
        private InvoiceStatus status;
        private LocalDateTime issuedAt;

        // -------------------------------------------------------
        // Financials
        // -------------------------------------------------------
        private BigDecimal baseAmount;
        private BigDecimal previousArrears;
        private BigDecimal lateFeeAmount;
        private BigDecimal totalAmount;
        private BigDecimal amountPaid;

        // -------------------------------------------------------
        // Line items — full charge breakdown
        // -------------------------------------------------------
        private List<InvoiceItemDTO> lineItems;

        // -------------------------------------------------------
        // Branding — sourced from society_settings
        // -------------------------------------------------------
        private String logoUrl;
        private String stampImageUrl;
        private String authorizedSignatoryName;

        // -------------------------------------------------------
        // Static factory
        //
        // Usage:
        //   SocietySettings settings = societySettingsRepo
        //       .findBySocietyId(invoice.getSociety().getId())
        //       .orElse(null);
        //   InvoiceResponse response = InvoiceResponse.from(invoice, settings);
        // -------------------------------------------------------
        public static InvoiceResponse from(Invoice invoice, SocietySettings settings) {

                // Map line items; guard against an uninitialised collection
                List<InvoiceItemDTO> items = (invoice.getLineItems() != null)
                        ? invoice.getLineItems()
                        .stream()
                        .map(InvoiceItemDTO::from)
                        .collect(Collectors.toList())
                        : Collections.emptyList();

                // Branding fields are nullable — settings row may not exist yet
                String logoUrl = null;
                String stampImageUrl = null;
                String authorizedSignatoryName = null;

                if (settings != null) {
                        logoUrl = settings.getLogoUrl();
                        stampImageUrl = settings.getStampImageUrl();
                        authorizedSignatoryName = settings.getAuthorizedSignatoryName();
                }

                return InvoiceResponse.builder()
                        // Identity
                        .invoiceId(invoice.getId())
                        .flatNumber(invoice.getFlat().getFlatNumber())
                        .wing(invoice.getFlat().getWing())
                        .name(invoice.getFlat().getUser().getName())
                        .email(invoice.getFlat().getUser().getEmail())
                        .billingMonth(invoice.getBillingMonth())
                        .dueDate(invoice.getDueDate())

                        // Metadata
                        .status(invoice.getStatus())
                        .issuedAt(invoice.getIssuedAt())

                        // Financials
                        .baseAmount(invoice.getBaseAmount())
                        .previousArrears(invoice.getPreviousArrears())
                        .lateFeeAmount(invoice.getLateFeeAmount())
                        .totalAmount(invoice.getTotalAmount())
                        .amountPaid(invoice.getAmountPaid())

                        // Line items
                        .lineItems(items)

                        // Branding
                        .logoUrl(logoUrl)
                        .stampImageUrl(stampImageUrl)
                        .authorizedSignatoryName(authorizedSignatoryName)

                        .build();
        }
}