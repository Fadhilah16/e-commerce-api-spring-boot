package com.ecommerce.models.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.ecommerce.models.ProductEntity;
import com.ecommerce.models.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="cart_items")
public class CartItemEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CartEntity cart;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity seller;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductEntity product;

    private double totalQuantity;

    private BigDecimal totalPrice;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonIgnore
    private Date updateAt;
   

   


    public CartItemEntity() {
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public CartEntity getCart() {
        return cart;
    }



    public void setCart(CartEntity cart) {
        this.cart = cart;
    }



    public UserEntity getSeller() {
        return seller;
    }



    public void setSeller(UserEntity seller) {
        this.seller = seller;
    }



    public ProductEntity getProduct() {
        return product;
    }



    public void setProduct(ProductEntity product) {
        this.product = product;
    }



    public double getTotalQuantity() {
        return totalQuantity;
    }



    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }



    public BigDecimal getTotalPrice() {
        return totalPrice;
    }



    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    public Date getCreatedAt() {
        return createdAt;
    }



    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }



    public Date getUpdateAt() {
        return updateAt;
    }



    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

   
 
   

    
    
}
