package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.ProductEntity;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long>{
    
}
