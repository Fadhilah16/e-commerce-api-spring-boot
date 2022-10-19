package com.ecommerce.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.models.order.OrderEntity;
import com.ecommerce.models.order.OrderItemEntity;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItemEntity, Long>{
    List<OrderItemEntity> findByOrder(OrderEntity order);
}
