package com.ecommerce.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.DTO.cart.CartItemRequestDTO;
import com.ecommerce.DTO.cart.CartResponseDTO;
import com.ecommerce.models.ProductEntity;
import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartEntity;
import com.ecommerce.models.cart.CartItemEntity;
import com.ecommerce.repository.ProductRepo;
import com.ecommerce.repository.cart.CartItemRepo;
import com.ecommerce.repository.cart.CartRepo;
import com.ecommerce.services.UserService.UserService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CartService {

    private CartRepo cartRepo;
    private CartItemRepo cartItemRepo;  
    private ProductRepo productRepo;   
    private UserService userService;


    public CartResponseDTO addToCart(HttpServletRequest request, CartItemRequestDTO cartItemRequest){
        ProductEntity product = productRepo.findById(cartItemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Error: Product is not found."));
        UserEntity user = userService.findUserByJwtUsername(request).get();
        Optional<CartEntity> cartOptional = cartRepo.findByUser(user);
        CartEntity responseCart;
       
        if (cartOptional.isPresent()){
            responseCart = cartOptional.get();
            responseCart = updateCart( request, cartItemRequest );  
        }else {
            responseCart = createCart(request, cartItemRequest );
            createCartItem(responseCart, cartItemRequest, product);

        }
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        List<CartItemEntity> cartItems = cartItemRepo.findByCart(responseCart);
        cartResponseDTO.setId(responseCart.getId());
        cartResponseDTO.setUser(responseCart.getUser());
        cartResponseDTO.setCartItems(cartItems);
        cartResponseDTO.setTotalItem((double)cartItems.size());
        cartResponseDTO.setTotalQuantity(responseCart.getTotalQuantity());
        cartResponseDTO.setTotalPrice(responseCart.getTotalPrice());
        return cartResponseDTO;
    }

    public CartEntity createCart(HttpServletRequest request, CartItemRequestDTO cartItemRequest ){
        ProductEntity product = productRepo.findById(cartItemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Error: Product is not found."));
        UserEntity user = userService.findUserByJwtUsername(request).get();

        CartEntity cart = new CartEntity();
        cart.setUser(user); 
        cart.setTotalQuantity(cartItemRequest.getQuantity());
        cart.setTotalItem((double) 1);
        cart.setTotalPrice(new BigDecimal((cartItemRequest.getQuantity())*(product.getPrice().doubleValue())));
        cart.setCreatedAt(new Date());
        cart.setUpdateAt(new Date());

        return cartRepo.save(cart);
    }

    public CartEntity createCart(UserEntity user){

        CartEntity cart = new CartEntity();
        cart.setUser(user); 
        cart.setTotalQuantity((double)0);
        cart.setTotalItem((double) 0);
        cart.setTotalPrice(new BigDecimal(0));
        cart.setCreatedAt(new Date());
        cart.setUpdateAt(new Date());

        return cartRepo.save(cart);
    }

    public CartEntity updateCart(HttpServletRequest request, CartItemRequestDTO cartItemRequest ){
        ProductEntity product = productRepo.findById(cartItemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Error: Product is not found."));
        UserEntity user = userService.findUserByJwtUsername(request).get();
        Optional<CartEntity> cartOptional = cartRepo.findByUser(user);
        CartEntity cart = cartOptional.get();
        Optional<CartItemEntity> cartItemOptional = cartItemRepo.findByProductAndCart(product, cart);

        CartItemEntity cartItem ;
            if (cartItemOptional.isPresent()){
                cartItem = cartItemOptional.get();
                
                if (cartItemRequest.getQuantity() == 0){
                    cart = deleteCartItemById(request, cartItem.getId());
                }else{
                    cart.setTotalQuantity(cart.getTotalQuantity() - cartItem.getTotalQuantity() + cartItemRequest.getQuantity());
                    cart.setTotalPrice(new BigDecimal(cart.getTotalPrice().doubleValue() - (cartItem.getTotalQuantity()*product.getPrice().doubleValue()) + (cartItemRequest.getQuantity())*(product.getPrice().doubleValue())));
                    cartItem.setTotalQuantity(cartItemRequest.getQuantity());
                    cartItem.setTotalPrice(new BigDecimal((cartItemRequest.getQuantity())*product.getPrice().doubleValue()));
                    updateCartItem(cartItem);
                }
  
                
            }else {
                createCartItem(cart, cartItemRequest, product);
            
                cart.setTotalItem(cart.getTotalItem() + 1);
                cart.setTotalQuantity(cart.getTotalQuantity() + cartItemRequest.getQuantity());
                cart.setTotalPrice(new BigDecimal(cart.getTotalPrice().doubleValue() + (cartItemRequest.getQuantity()*product.getPrice().doubleValue())) );
            }
            
            
        
            cart.setUpdateAt(new Date());
            return cartRepo.save(cart);

    }

    public CartResponseDTO findCart(HttpServletRequest request){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        CartEntity cart = cartRepo.findByUser(user).get();
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        List<CartItemEntity> cartItems = cartItemRepo.findByCart(cart);

        cartResponseDTO.setId(cart.getId());
        cartResponseDTO.setUser(user);
        cartResponseDTO.setTotalQuantity(cart.getTotalQuantity());
        cartResponseDTO.setTotalPrice(cart.getTotalPrice());
        cartResponseDTO.setTotalItem(cart.getTotalItem());
        cartResponseDTO.setCartItems(cartItems);

        return cartResponseDTO;
    }

    public List<CartItemEntity> findAllCartItems(HttpServletRequest request){
        UserEntity user = userService.findUserByJwtUsername(request).get();
        Optional<CartEntity> cartOptional = cartRepo.findByUser(user);
        CartEntity cart = cartOptional.get();
    
        List<CartItemEntity> cartItems = cartItemRepo.findByCart(cart);

        return cartItems;
    }


    public  CartItemEntity findCartItemById(HttpServletRequest request, long cartItemId ){
        Optional<UserEntity> userOptional = userService.findUserByJwtUsername(request);
        Optional<CartEntity> cartOptional = cartRepo.findByUser(userOptional.get());
        Optional<CartItemEntity> cartItemOptional = cartItemRepo.findById(cartItemId);
        if (cartOptional.get().equals(cartItemOptional.get().getCart())){
            return cartItemOptional.get();

        }
        return null;
         
    }

    public CartItemEntity createCartItem( CartEntity cart, CartItemRequestDTO cartItemRequest, ProductEntity product){
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setSeller(product.getUser());
        cartItem.setTotalQuantity(cartItemRequest.getQuantity());
        cartItem.setTotalPrice(new BigDecimal((cartItemRequest.getQuantity())*(product.getPrice().doubleValue())));
        cartItem.setCreatedAt(new Date());
        cartItem.setUpdateAt(new Date());
        return cartItemRepo.save(cartItem);
    }

    public CartItemEntity updateCartItem(CartItemEntity cartItem){
        return cartItemRepo.save(cartItem);
    }

    public void deleteCartItemByProductId(long id){
        ProductEntity product = productRepo.findById(id).get();
        cartItemRepo.deleteByProduct(product);
        
    }

    public void deleteCart(long id){

        cartRepo.deleteById(id);
    }


    public CartEntity deleteCartItemById(HttpServletRequest request, long id){
        Optional<UserEntity> userOptional = userService.findUserByJwtUsername(request);
        Optional<CartEntity> cartOptional = cartRepo.findByUser(userOptional.get());
        Optional<CartItemEntity> cartItemOptional = cartItemRepo.findById(id);

        if (cartOptional.get().equals(cartItemOptional.get().getCart())){
            cartItemRepo.deleteById(id);
            cartOptional.get().setTotalItem(cartOptional.get().getTotalItem() - 1);
            cartOptional.get().setTotalQuantity(cartOptional.get().getTotalQuantity() - cartItemOptional.get().getTotalQuantity());
            cartOptional.get().setTotalPrice(new BigDecimal(cartOptional.get().getTotalPrice().doubleValue() - cartItemOptional.get().getTotalPrice().doubleValue()) );
            
        }
        return cartRepo.save(cartOptional.get());
    }
    
    
   

    
}
