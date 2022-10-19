package com.ecommerce.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.UserEntity;
import com.ecommerce.models.order.OrderEntity;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long>{
    public List<OrderEntity> findByCustomer(UserEntity user);
    public List<OrderEntity> findBySeller(UserEntity seller);
}
