package com.skinplus.order_service.client;

import com.skinplus.order_service.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    String processPayment(PaymentRequest request);
}