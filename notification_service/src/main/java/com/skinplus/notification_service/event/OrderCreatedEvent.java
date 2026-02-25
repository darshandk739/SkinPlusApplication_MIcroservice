package com.skinplus.notification_service.event;


public class OrderCreatedEvent {

    private Long orderId;
    private Long productId;
    private int quantity;

    public OrderCreatedEvent() {}

    public Long getOrderId() { return orderId; }
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}