package com.ecommerce.DTO.cart;

import javax.validation.constraints.NotNull;

public class CartItemRequestDTO {
    @NotNull
    private Long productId;
    @NotNull
    private Double quantity;

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    
}
