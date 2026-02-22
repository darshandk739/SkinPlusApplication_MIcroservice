package com.skinplus.order_service.event;

public class OrderCreatedEvent {

	private Long orderId;
	private Long productId;
	private Integer quantity;

	public OrderCreatedEvent() {
	}

	public OrderCreatedEvent(Long orderId, Long productId, Integer quantity) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getOrderId() {
		return orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public Integer getQuantity() {
		return quantity;
	}
}