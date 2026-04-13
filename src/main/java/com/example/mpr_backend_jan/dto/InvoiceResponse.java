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

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

        private Long invoiceId;
        private String flatNumber;
        private String wing;
        private String name;
        private String email;
        private LocalDate billingMonth;
        private LocalDate dueDate;

        private InvoiceStatus status;
        private LocalDateTime issuedAt;

        private BigDecimal baseAmount;
        private BigDecimal previousArrears;
        private BigDecimal lateFeeAmount;
        private BigDecimal totalAmount;
        private BigDecimal amountPaid;

        private List<InvoiceItemDTO> lineItems;

        private String logoUrl;
        private String stampImageUrl;
        private String authorizedSignatoryName;

        private String societyName;
        private String societyAddress;

        public static InvoiceResponse from(
                Invoice invoice,
                SocietySettings settings,
                List<InvoiceItemDTO> items
        ) {
                String logoUrl = null;
                String stampImageUrl = null;
                String authorizedSignatoryName = null;

                if (settings != null) {
                        logoUrl = settings.getLogoUrl();
                        stampImageUrl = settings.getStampImageUrl();
                        authorizedSignatoryName = settings.getAuthorizedSignatoryName();
                }

                String societyName = null;
                String societyAddress = null;

                if (invoice.getSociety() != null) {
                        societyName = invoice.getSociety().getSocietyName();
                        societyAddress = invoice.getSociety().getAddress();
                }

                return InvoiceResponse.builder()
                        .invoiceId(invoice.getId())
                        .flatNumber(invoice.getFlat().getFlatNumber())
                        .wing(invoice.getFlat().getWing())
                        .name(invoice.getFlat().getUser().getName())
                        .email(invoice.getFlat().getUser().getEmail())
                        .billingMonth(invoice.getBillingMonth())
                        .dueDate(invoice.getDueDate())

                        .status(invoice.getStatus())
                        .issuedAt(invoice.getIssuedAt())

                        .baseAmount(invoice.getBaseAmount())
                        .previousArrears(invoice.getPreviousArrears())
                        .lateFeeAmount(invoice.getLateFeeAmount())
                        .totalAmount(invoice.getTotalAmount())
                        .amountPaid(invoice.getAmountPaid())

                        .lineItems(items != null ? items : Collections.emptyList())

                        .logoUrl(logoUrl)
                        .stampImageUrl(stampImageUrl)
                        .authorizedSignatoryName(authorizedSignatoryName)

                        .societyName(societyName)
                        .societyAddress(societyAddress)

                        .build();
        }
}