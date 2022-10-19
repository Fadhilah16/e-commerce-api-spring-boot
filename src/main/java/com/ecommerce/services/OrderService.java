package com.ecommerce.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.DTO.order.OrderItemRequestDTO;
import com.ecommerce.DTO.order.OrderRequestDTO;
import com.ecommerce.DTO.order.PaymentRequestDTO;
import com.ecommerce.models.ProductEntity;
import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartEntity;
import com.ecommerce.models.cart.CartItemEntity;
import com.ecommerce.models.enums.OrderStatus;
import com.ecommerce.models.order.OrderEntity;
import com.ecommerce.models.order.OrderItemEntity;
import com.ecommerce.repository.ProductRepo;
import com.ecommerce.repository.cart.CartItemRepo;
import com.ecommerce.repository.cart.CartRepo;
import com.ecommerce.repository.order.OrderItemRepo;
import com.ecommerce.repository.order.OrderRepo;
import com.ecommerce.services.UserService.UserService;

@Service
@Transactional
public class OrderService {

    private OrderRepo orderRepo;

    private OrderItemRepo orderItemRepo;

    private UserService userService;
  
    private CartItemRepo cartItemRepo;
   
    private CartRepo cartRepo;

    private ProductRepo productRepo;
   
    public OrderService(OrderRepo orderRepo, OrderItemRepo orderItemRepo, UserService userService, CartItemRepo cartItemRepo, CartRepo cartRepo, ProductRepo productRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.userService = userService;
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }



    public List<OrderEntity> createOrder(HttpServletRequest request, OrderRequestDTO orderRequestDTO  ){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        CartEntity cart = cartRepo.findByUser(user).get();
        List<UserEntity> sellers = cartItemRepo.findDistinctSellerByCart(cart);
        List<OrderEntity> orderList = new ArrayList<>();
        for (UserEntity seller : sellers) {
            OrderEntity order = new OrderEntity();
            
            
            for (OrderItemRequestDTO orderItemRequest : orderRequestDTO.getOrderItems()) {
                Optional<CartItemEntity> cartItem = cartItemRepo.findByIdAndSeller(orderItemRequest.getCartItemId(), seller);
                
                if (cartItem.isPresent()){
                    if (order.getId() == null ){
                        order.setName(orderRequestDTO.getName());
                        order.setCustomer(user);
                        order.setEmail(orderRequestDTO.getEmail());
                        order.setAddress(orderRequestDTO.getAddress());
                        order.setSeller(seller);
                        order.setTotalItem((double)0);
                        order.setTotalQuantity((double)0);
                        order.setTotalPriceOrder(new BigDecimal(0));
                        order.setCreatedAt(new Date());
                        order.setUpdateAt(new Date());
                        order = orderRepo.save(order);
                    }
                    

                    OrderItemEntity orderItem = new OrderItemEntity();
                    OrderItemRequestDTO filteredOrderItemRequest = orderRequestDTO.getOrderItems().stream().filter(o -> o.getCartItemId() == cartItem.get().getId()).findAny().orElse(null);
                    order.setShippingCost(filteredOrderItemRequest.getShippingCost());
                    order.setTotalItem(order.getTotalItem()+1);
                    order.setTotalQuantity(order.getTotalQuantity()+cartItem.get().getTotalQuantity());
                    order.setTotalPriceOrder(new BigDecimal(order.getTotalPriceOrder().doubleValue() + cartItem.get().getTotalPrice().doubleValue()));
                    orderItem.setCartItem(cartItem.get());
                    orderItem.setOrder(order);

                    ProductEntity product = cartItem.get().getProduct();
                    if (cartItem.get().getTotalQuantity() > product.getStock()){
                        throw new RuntimeException("quantity exceeds"+ product.getName() +"stock");
                    }
                    product.setStock(product.getStock() - cartItem.get().getTotalQuantity());
                    productRepo.save(product);
                    orderItemRepo.save(orderItem);
                }

                
            }
            
           if (order.getId() != null ){
            order.setStatus(OrderStatus.DRAFT);
            order.setTotalPay(new BigDecimal(order.getTotalPriceOrder().doubleValue() + order.getShippingCost().doubleValue()));
            order = orderRepo.save(order);
            orderList.add(order);
           }
            
        }

        return orderList;
    }

    public List<OrderEntity> findAllCustomerOrders(HttpServletRequest request){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        return orderRepo.findByCustomer(user);
    }

    public List<OrderEntity> findAllOrdersBySeller(HttpServletRequest request){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        return orderRepo.findBySeller(user);
    }

    public OrderEntity cancelOrder(HttpServletRequest request, Long orderId ){
        UserEntity user = userService.findUserByJwtUsername(request).get();

        OrderEntity order = orderRepo.findById(orderId).get();

        if (!order.getSeller().equals(user) && !order.getCustomer().equals(user)){
            throw new RuntimeException("You can't cancel this order");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.COMPLETED ){
            throw new RuntimeException("You can't cancel this order due to the order status has been " + order.getStatus());
        }

        List<OrderItemEntity> orderItems = orderItemRepo.findByOrder(order);

        for (OrderItemEntity orderItem : orderItems) {
            orderItem.getCartItem().getProduct().setStock( orderItem.getCartItem().getProduct().getStock() + orderItem.getCartItem().getTotalQuantity());
            productRepo.save(orderItem.getCartItem().getProduct());
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdateAt(new Date());

        return orderRepo.save(order);
    }

    public OrderEntity acceptOrder(HttpServletRequest request, Long orderId){
        UserEntity user = userService.findUserByJwtUsername(request).get();

        OrderEntity order = orderRepo.findById(orderId).get();

        if (!order.getSeller().equals(user)){
            throw new RuntimeException("You are not the seller of this order");
        }

        if (order.getStatus() == OrderStatus.CANCELED){
            throw new RuntimeException("Cannot accept the order. The order has been canceled");
        }
        
        if (order.getStatus() == OrderStatus.DRAFT){
            order.setStatus(OrderStatus.ORDERED);
            order.setUpdateAt(new Date());

            return orderRepo.save(order);
        }
        throw new RuntimeException("Customer has not placed an order");
        
    }

    public OrderEntity payOrder(HttpServletRequest request, PaymentRequestDTO payment){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        OrderEntity order = orderRepo.findById(payment.getOrderId()).get();

        if (!order.getCustomer().equals(user)){
            throw new RuntimeException("You are not the customer of this order");
        }
        if (order.getStatus() == OrderStatus.CANCELED){
            throw new RuntimeException("Cannot pay the order. The order has been canceled");
        }

        if ( order.getStatus() != OrderStatus.ORDERED){
            throw new RuntimeException("Cannot pay the order. Seller has not accepted the order");
        }

        if (order.getTotalPay().doubleValue() == payment.getNominal().doubleValue()){
            order.setStatus(OrderStatus.PAID);
            order.setUpdateAt(new Date());

            return orderRepo.save(order);
        }
        
        throw new RuntimeException("Your nominal is not equal to total price");
        
        
       
    }
    
    public OrderEntity shipOrder(HttpServletRequest request, Long orderId){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        OrderEntity order = orderRepo.findById(orderId).get();

        if (!order.getSeller().equals(user)){
            throw new RuntimeException("You are not the seller of this order");
        }

        if (order.getStatus() == OrderStatus.CANCELED){
            throw new RuntimeException("Cannot ship the order. The order has been canceled");
        }

        if (order.getStatus() == OrderStatus.PAID){
            order.setStatus(OrderStatus.SHIPPED);
            order.setUpdateAt(new Date());

            return orderRepo.save(order);
        }
        throw new RuntimeException("Customer has not paid the invoice");
    }

    public OrderEntity completeOrder(HttpServletRequest request, Long orderId){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        OrderEntity order = orderRepo.findById(orderId).get();

        if (!order.getSeller().equals(user)){
            throw new RuntimeException("You are not the seller of this order");
        }

        if (order.getStatus() == OrderStatus.CANCELED){
            throw new RuntimeException("Cannot complete the order. The order has been canceled");
        }

        if (order.getStatus() == OrderStatus.SHIPPED){
            order.setStatus(OrderStatus.COMPLETED);
            order.setUpdateAt(new Date());

            return orderRepo.save(order);
        }
        throw new RuntimeException("The order has not arrived.");
    }
}
