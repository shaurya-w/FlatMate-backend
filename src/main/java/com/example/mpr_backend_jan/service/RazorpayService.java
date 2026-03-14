package com.example.mpr_backend_jan.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.Utils;

@Service
public class RazorpayService {

    private final RazorpayClient razorpayClient;
    private final String razorpayKeySecret;

    public RazorpayService(
            RazorpayClient razorpayClient,
            @Value("${razorpay.key.secret}") String razorpayKeySecret) {

        this.razorpayClient = razorpayClient;
        this.razorpayKeySecret = razorpayKeySecret;

        //System.out.println("Loaded Razorpay Secret: " + razorpayKeySecret);
    }

    public Order createOrder(Long amount) throws Exception {

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amount); // in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        return razorpayClient.orders.create(orderRequest);
    }

    public boolean verifyPaymentSignature(
            String orderId,
            String paymentId,
            String signature) throws Exception {

        JSONObject options = new JSONObject();

        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        return Utils.verifyPaymentSignature(options, razorpayKeySecret);
    }
}
