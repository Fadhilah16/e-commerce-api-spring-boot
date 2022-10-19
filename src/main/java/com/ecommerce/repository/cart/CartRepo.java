package com.ecommerce.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartEntity;

@Repository
public interface CartRepo extends JpaRepository<CartEntity, Long>{
   
    public Optional<CartEntity> findByUser(UserEntity user);
}
