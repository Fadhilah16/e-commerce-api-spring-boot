package com.ecommerce.repository.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.ProductEntity;
import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartEntity;
import com.ecommerce.models.cart.CartItemEntity;

@Repository
public interface CartItemRepo extends JpaRepository<CartItemEntity, Long> {
    public Optional<CartItemEntity> findByProductAndCart(ProductEntity product, CartEntity cart);
    public Optional<CartItemEntity> findByIdAndSeller(Long id, UserEntity seller);
    public Optional<CartItemEntity> findByCartAndSeller(CartEntity cart, UserEntity user);
    public List<CartItemEntity> findByCart(CartEntity cart);
    public void deleteByProduct(ProductEntity product);
    // public List<CartItemEntity> findDistinctSellerByCart(CartEntity cart);
    @Query("SELECT DISTINCT c.seller FROM CartItemEntity c WHERE c.cart = :cart")
    public List<UserEntity> findDistinctSellerByCart(@Param("cart") CartEntity cart);
    
}
