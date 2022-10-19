package com.ecommerce.DTO.order;

import java.math.BigDecimal;

public class OrderItemRequestDTO {
    private Long cartItemId;
    private BigDecimal shippingCost;
    
   
    public BigDecimal getShippingCost() {
        return shippingCost;
    }
    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }
    public Long getCartItemId() {
        return cartItemId;
    }
    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    
}
