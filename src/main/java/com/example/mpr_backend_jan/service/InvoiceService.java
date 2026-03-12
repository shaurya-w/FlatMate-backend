package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.InvoiceResponse;
import com.example.mpr_backend_jan.model.Invoice;
import com.example.mpr_backend_jan.model.SocietySettings;
import com.example.mpr_backend_jan.repository.InvoiceRepository;
import com.example.mpr_backend_jan.repository.SocietySettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<InvoiceResponse> getInvoicesForMail(List<Long> userIds) {

        List<Invoice> invoices = invoiceRepository.findInvoicesByUserIds(userIds);

        return invoices.stream()
                .map(inv -> InvoiceResponse.from(inv, null))
                .toList();
    }
}