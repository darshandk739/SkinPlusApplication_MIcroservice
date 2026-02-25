package com.skinplus.order_service.dto;

public class PaymentRequest {

    private Long orderId;
    private Double amount;

    public PaymentRequest() {}

    public PaymentRequest(Long orderId, Double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public Long getOrderId() { return orderId; }
    public Double getAmount() { return amount; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setAmount(Double amount) { this.amount = amount; }
}