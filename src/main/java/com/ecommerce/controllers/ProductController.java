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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTO.ResponseDTO;
import com.ecommerce.models.ProductEntity;
import com.ecommerce.services.ProductService;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ResponseDTO<ProductEntity>> createProduct(HttpServletRequest request, @Valid @RequestBody ProductEntity product, Errors errors) {

        ResponseDTO<ProductEntity> response = new ResponseDTO<>();
        
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.setEntity(productService.createProduct(product, request));
        response.setStatus("201");
        response.setMessages(new ArrayList<>(){{add("Product created successfully");}});
    
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @GetMapping
    public Iterable<ProductEntity> findAllProducts(){
        return productService.findAllProduct();
    }

    @GetMapping("/{id}")
    public ProductEntity findOneProduct(@PathVariable("id") long id){
        return productService.findProductById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<ResponseDTO<ProductEntity>> updateProduct(@Valid @RequestBody ProductEntity product, Errors errors){
        ResponseDTO<ProductEntity> response = new ResponseDTO<>();
        
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.setStatus("204");
        response.setMessages(new ArrayList<>(){{add("Product updated successfully");}});
        response.setEntity(productService.updateProduct(product));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") long id){
        productService.deleteProductById(id);
    }
}
