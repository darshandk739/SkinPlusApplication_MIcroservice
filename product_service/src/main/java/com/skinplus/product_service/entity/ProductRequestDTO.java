package com.skinplus.product_service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ProductRequestDTO implements Serializable {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Brand is required")
	private String brand;

	@NotBlank(message = "Category is required")
	private String category;

	@NotNull(message = "Price required")
	@Positive(message = "Price must be positive")
	private BigDecimal price;

	@NotNull
	@Min(value = 0, message = "Stock cannot be negative")
	private Integer stock;

	private String description;
	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@NotBlank
	private String skinType;

	@NotNull
	private LocalDate mfgDate;

	@NotNull
	private LocalDate expDate;
}