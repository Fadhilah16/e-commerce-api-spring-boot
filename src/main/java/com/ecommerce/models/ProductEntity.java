package com.ecommerce.models;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
 
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity  
@Table(name="products")
public class ProductEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     @NotBlank(message="name is required")
     @NotNull(message="name is required")
     @Column(length = 100)
     private String name;
 
     @NotNull(message="description is required")
     @Column(length = 500)
     private String description;
 
   
     @NotNull(message="price is required")
     private BigDecimal price;

     @NotNull(message="Stock is required")
     private Double stock;

    @ManyToOne
     private UserEntity user;

     @CreatedDate
     @Column(name = "created_at", nullable = false, updatable = false)
     @JsonIgnore
     private Date createdAt;
 
     @LastModifiedDate
     @Column(name = "updated_at")
     @JsonIgnore
     private Date updateAt;

   


    public ProductEntity() {
    }



    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
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
