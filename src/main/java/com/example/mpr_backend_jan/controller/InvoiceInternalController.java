package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.InvoiceResponse;
import com.example.mpr_backend_jan.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/invoices")
@RequiredArgsConstructor
public class InvoiceInternalController {

    private final InvoiceService invoiceService;

    @PostMapping("/for-mail")
    public List<InvoiceResponse> getInvoicesForMail(
            @RequestBody List<Long> invoiceIds
    ) {
        //System.out.println("Spring endpoint HIT with IDs: " + invoiceIds);
        return invoiceService.getInvoicesForMail(invoiceIds);
    }
}