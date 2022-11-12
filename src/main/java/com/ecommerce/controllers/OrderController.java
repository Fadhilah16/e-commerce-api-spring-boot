package com.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTO.ResponseDTO;
import com.ecommerce.DTO.order.OrderRequestDTO;
import com.ecommerce.DTO.order.PaymentRequestDTO;
import com.ecommerce.models.order.OrderEntity;
import com.ecommerce.services.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/orders")
@PreAuthorize("isAuthenticated()")
@AllArgsConstructor
public class OrderController {
    
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseDTO<List<OrderEntity>>> createOrder(HttpServletRequest request, @Valid @RequestBody OrderRequestDTO orderRequestDTO, Errors errors){
        ResponseDTO<List<OrderEntity>> response = new ResponseDTO<>();
        
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    
        response.setEntity(orderService.createOrder(request, orderRequestDTO));
        response.setStatus("201");
        response.setMessages(new ArrayList<>(){{add("order created successfully");}});
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    
    @GetMapping("/customer")
    public List<OrderEntity> findAllCustomerOrders(HttpServletRequest request){
        return orderService.findAllCustomerOrders(request);
    }

    @GetMapping("/seller")
    public List<OrderEntity> findAllIncomingOrders(HttpServletRequest request){
        return orderService.findAllOrdersBySeller(request);
    }

    @PutMapping("/cancel/{id}")
    public OrderEntity cancelOrder(HttpServletRequest request, @PathVariable("id") Long orderId  ){
        return orderService.cancelOrder(request, orderId );
    }

    @PutMapping("/seller/accept/{id}")
    public OrderEntity acceptOrder(HttpServletRequest request, @PathVariable("id") Long orderId){
        return orderService.acceptOrder(request, orderId);
    }

    @PutMapping("/customer/payment")
    public OrderEntity payOrder(HttpServletRequest request, @RequestBody PaymentRequestDTO payment){
        return orderService.payOrder(request, payment);
    }

    @PutMapping("/seller/shipment/{id}")
    public OrderEntity shipOrder(HttpServletRequest request, @PathVariable("id") Long orderId ){
        return orderService.shipOrder(request, orderId);
    }

    @PutMapping("/seller/complete/{id}")
    public OrderEntity completeOrder(HttpServletRequest request, @PathVariable("id") Long orderId ){
        return orderService.completeOrder(request, orderId);
    }

}
