package com.ecommerce.DTO.cart;

import java.math.BigDecimal;
import java.util.List;

import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartItemEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private Long id;

    private UserEntity user;


    private List<CartItemEntity> cartItems;

    private Double totalQuantity;

    private Double totalItem;
  

    private BigDecimal totalPrice;


}
