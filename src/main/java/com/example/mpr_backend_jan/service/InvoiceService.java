package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.InvoiceResponse;
import com.example.mpr_backend_jan.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    // Fetch invoices for emails
    public List<InvoiceResponse> getInvoicesForMail(List<Long> invoiceIds) {
        return invoiceRepository.findInvoicesByIds(invoiceIds);
    }
}