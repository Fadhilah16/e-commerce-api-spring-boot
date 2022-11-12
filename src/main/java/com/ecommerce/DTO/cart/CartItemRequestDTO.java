package com.ecommerce.DTO.cart;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDTO {
    @NotNull
    private Long productId;
    @NotNull
    private Double quantity;

    
}
