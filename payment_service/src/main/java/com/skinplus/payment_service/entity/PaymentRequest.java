package com.skinplus.payment_service.entity;

public class PaymentRequest {

    private Long orderId;
    private Double amount;

    public PaymentRequest() {}

    public Long getOrderId() { return orderId; }
    public Double getAmount() { return amount; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setAmount(Double amount) { this.amount = amount; }
}
