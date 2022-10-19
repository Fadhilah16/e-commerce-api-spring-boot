package com.ecommerce.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTO.ResponseDTO;
import com.ecommerce.DTO.cart.CartItemRequestDTO;
import com.ecommerce.DTO.cart.CartResponseDTO;
import com.ecommerce.models.cart.CartEntity;
import com.ecommerce.models.cart.CartItemEntity;
import com.ecommerce.services.CartService;


@RestController
@RequestMapping("api/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    private CartService cartService;

  
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CartResponseDTO>> addToCart(HttpServletRequest request, @Valid @RequestBody CartItemRequestDTO cartItemRequestDTO, Errors errors){
        ResponseDTO<CartResponseDTO> response = new ResponseDTO<>();
        
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } 
        response.setStatus("201");
        response.setMessages(new ArrayList<>(){{add("Item successfully added to cart");}});
        response.setEntity(cartService.addToCart(request, cartItemRequestDTO));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping
    public CartResponseDTO findCart(HttpServletRequest request){
        return cartService.findCart(request);
    }
    
    @GetMapping("items/{id}")
    public ResponseDTO<CartItemEntity> findOneItem(HttpServletRequest request, @PathVariable("id") long cartItemId){
        ResponseDTO<CartItemEntity> response = new ResponseDTO<>();
        try{
            CartItemEntity cartItem = cartService.findCartItemById(request, cartItemId);
            response.setStatus("200");
            response.setMessages(null);
            response.setEntity(cartItem);
        }catch (RuntimeException e){
            response.setStatus("400");
            response.setMessages(new ArrayList<>(){{add(e.getMessage().toString());}});
            response.setEntity(null);
        }
         return response;
    }

    @DeleteMapping("items/{id}")
    public CartEntity deleteCartItem(HttpServletRequest request, @PathVariable("id") long id){
        return cartService.deleteCartItemById(request, id);
    }

  
}
