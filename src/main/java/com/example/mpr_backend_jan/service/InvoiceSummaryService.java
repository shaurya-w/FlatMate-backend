package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.InvoiceSummaryDTO;
import com.example.mpr_backend_jan.repository.InvoiceRepository;
import com.example.mpr_backend_jan.repository.InvoiceSummaryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceSummaryService {

    private final InvoiceSummaryRepository invoiceSummaryRepository;

    public InvoiceSummaryService(InvoiceSummaryRepository invoiceSummaryRepository) {
        this.invoiceSummaryRepository = invoiceSummaryRepository;
    }

    public List<InvoiceSummaryDTO> getPendingInvoices(Long userId) {
        return invoiceSummaryRepository.findPendingInvoicesByUser(
                userId,
                PageRequest.of(0, 5) // max 5
        );
    }
}