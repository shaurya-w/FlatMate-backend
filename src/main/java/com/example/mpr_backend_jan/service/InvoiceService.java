package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.InvoiceItemDTO;
import com.example.mpr_backend_jan.dto.InvoiceResponse;
import com.example.mpr_backend_jan.model.Invoice;
import com.example.mpr_backend_jan.model.SocietyCharge;
import com.example.mpr_backend_jan.model.SocietySettings;
import com.example.mpr_backend_jan.repository.InvoiceRepository;
import com.example.mpr_backend_jan.repository.SocietyChargeRepository;
import com.example.mpr_backend_jan.repository.SocietySettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SocietySettingsRepository societySettingsRepository;
    private final SocietyChargeRepository societyChargeRepository;

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesForMail(List<Long> userIds) {

        List<Invoice> invoices = invoiceRepository.findUnpaidInvoicesByUserIds(userIds);

        return invoices.stream()
                .map(invoice -> {
                    SocietySettings settings = societySettingsRepository
                            .findBySocietyId(invoice.getSociety().getId())
                            .orElse(null);

                    List<SocietyCharge> charges = societyChargeRepository
                            .findActiveChargesBySocietyId(invoice.getSociety().getId());

                    List<InvoiceItemDTO> items = charges.stream()
                            .map(charge -> InvoiceItemDTO.builder()
                                    .description(charge.getChargeType().getName())
                                    .amount(charge.getAmount())
                                    .build())
                            .toList();

                    return InvoiceResponse.from(invoice, settings, items);
                })
                .toList();
    }
}