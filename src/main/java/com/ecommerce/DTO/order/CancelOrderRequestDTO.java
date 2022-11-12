package com.ecommerce.DTO.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderRequestDTO {
    private Long orderId;
    private String reason;
    
}
