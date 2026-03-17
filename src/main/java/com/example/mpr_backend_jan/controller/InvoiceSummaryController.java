package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.service.InvoiceService;
import com.example.mpr_backend_jan.service.InvoiceSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceSummaryController {

    private final InvoiceSummaryService invoiceSummaryService;

    public InvoiceSummaryController(InvoiceSummaryService invoiceSummaryService) {
        this.invoiceSummaryService = invoiceSummaryService;
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<?> getPendingInvoices(@PathVariable Long userId) {
        return ResponseEntity.ok(invoiceSummaryService.getPendingInvoices(userId));
    }
}