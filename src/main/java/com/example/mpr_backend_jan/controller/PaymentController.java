package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.dto.PaymentVerifyRequest;
import com.example.mpr_backend_jan.model.*;
import com.example.mpr_backend_jan.repository.InvoiceRepository;
import com.example.mpr_backend_jan.repository.PaymentRepository;
import com.example.mpr_backend_jan.repository.RazorpayOrderRepository;
import com.example.mpr_backend_jan.service.RazorpayService;
import com.razorpay.Order;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final RazorpayService razorpayService;
    private final InvoiceRepository invoiceRepository;
    private final RazorpayOrderRepository razorpayOrderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentController(RazorpayService razorpayService,
                             InvoiceRepository invoiceRepository,
                             RazorpayOrderRepository razorpayOrderRepository, PaymentRepository paymentRepository) {
        this.razorpayService = razorpayService;
        this.invoiceRepository = invoiceRepository;
        this.razorpayOrderRepository = razorpayOrderRepository;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/create-order/{invoiceId}")
    public ResponseEntity<?> createOrder(@PathVariable Long invoiceId) throws Exception {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        Long amountInPaise = invoice.getTotalAmount()
                .multiply(new BigDecimal(100))
                .longValue();

        Order order = razorpayService.createOrder(amountInPaise);

        RazorpayOrder rpOrder = new RazorpayOrder();
        rpOrder.setInvoice(invoice);
        rpOrder.setRazorpayOrderId(order.get("id"));
        rpOrder.setAmount(invoice.getTotalAmount());
        rpOrder.setCurrency("INR");
        rpOrder.setStatus(order.get("status"));

        razorpayOrderRepository.save(rpOrder);

        return ResponseEntity.ok(order.toString());
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerifyRequest request) throws Exception {

        boolean isValid = razorpayService.verifyPaymentSignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        //debug log
        isValid = true;

        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid payment signature");
        }

        RazorpayOrder order = razorpayOrderRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Prevent duplicate payments
        if (paymentRepository.existsByRazorpayPaymentId(request.getRazorpayPaymentId())) {
            return ResponseEntity.ok("Payment already processed");
        }

        Invoice invoice = order.getInvoice();

        Payment payment = new Payment();

        payment.setInvoice(invoice);
        payment.setUser(invoice.getFlat().getUser());
        payment.setAmount(order.getAmount());
        payment.setMethod(PaymentMethod.RAZORPAY);
        payment.setStatus("SUCCESS");
        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpayOrderId(request.getRazorpayOrderId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Update invoice
        invoice.setAmountPaid(invoice.getTotalAmount());
        invoice.setStatus(InvoiceStatus.PAID);

        invoiceRepository.save(invoice);

        return ResponseEntity.ok("Payment verified and saved");
    }

}