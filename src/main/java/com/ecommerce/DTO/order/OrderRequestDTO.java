package com.ecommerce.DTO.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private String name;
    private String address;
    private String email;
    private List<OrderItemRequestDTO> orderItems;

}
