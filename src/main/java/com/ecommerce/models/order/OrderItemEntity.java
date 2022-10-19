package com.ecommerce.models.order;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ecommerce.models.cart.CartItemEntity;

@Entity
@Table(name = "order_items")
public class OrderItemEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private CartItemEntity cartItem;

    @ManyToOne
    private OrderEntity order;

    

    public OrderItemEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartItemEntity getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItemEntity cartItem) {
        this.cartItem = cartItem;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    

}
