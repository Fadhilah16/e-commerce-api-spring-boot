package com.ecommerce.DTO.cart;

import java.math.BigDecimal;
import java.util.List;

import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartItemEntity;

public class CartResponseDTO {
    private Long id;

    private UserEntity user;


    private List<CartItemEntity> cartItems;

    private Double totalQuantity;

    private Double totalItem;
  

    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }


    public List<CartItemEntity> getCartItem() {
        return cartItems;
    }

    public void setCartItems(List<CartItemEntity> cartItems) {
        this.cartItems = cartItems;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

      
    public Double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Double totalItems) {
        this.totalItem = totalItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    


}
