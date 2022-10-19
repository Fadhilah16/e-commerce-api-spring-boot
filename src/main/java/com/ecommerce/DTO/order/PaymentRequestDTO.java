package com.ecommerce.DTO.order;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    
    private Long orderId;
    private BigDecimal nominal;
    
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public BigDecimal getNominal() {
        return nominal;
    }
    public void setNominal(BigDecimal nominal) {
        this.nominal = nominal;
    }

    

}
