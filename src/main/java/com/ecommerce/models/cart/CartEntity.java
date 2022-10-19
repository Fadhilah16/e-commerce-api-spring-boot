package com.ecommerce.models.cart;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.ecommerce.models.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="carts")
public class CartEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    private Double totalItem;

    private Double totalQuantity;
    
    private BigDecimal totalPrice;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonIgnore
    private Date updateAt;

    

    public CartEntity() {
    }



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



    public Double getTotalItem() {
        return totalItem;
    }



    public void setTotalItem(Double totalItem) {
        this.totalItem = totalItem;
    }



    public Double getTotalQuantity() {
        return totalQuantity;
    }



    public void setTotalQuantity(Double totalQuantity) {
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
