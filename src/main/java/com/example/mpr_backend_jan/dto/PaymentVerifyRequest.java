package com.example.mpr_backend_jan.dto;

import lombok.Data;

@Data
public class PaymentVerifyRequest {

    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;

}