package com.ecommerce.controllers;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTO.ResponseDTO;
import com.ecommerce.DTO.auth.LoginRequestDTO;
import com.ecommerce.DTO.auth.RegisterRequestDTO;
import com.ecommerce.models.UserEntity;
import com.ecommerce.services.CartService;
import com.ecommerce.services.UserService.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private PasswordEncoder passwordEncoder;
	private UserService userService;
    private CartService cartService;


   

	public AuthController(PasswordEncoder passwordEncoder, UserService userService, CartService cartService) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.cartService = cartService;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequestDTO loginRequest, Errors errors) {
		ResponseDTO<?> response = new ResponseDTO<>();
		if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
		return ResponseEntity.ok(userService.userIsAuthenticated(loginRequest));
	}

    @PostMapping("/signup")
	public ResponseEntity<?> SignUp(@Valid @RequestBody RegisterRequestDTO registerRequest,  Errors errors) {
		ResponseDTO<?> response = new ResponseDTO<>();

		if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
		
		if (userService.existsByUsername(registerRequest.getUsername())) {
			
			response.setStatus("400");
			response.setMessages(new ArrayList<String>(){{
				add("Error: Username is already taken!");
			}});
			response.setEntity(null);

			return ResponseEntity.badRequest().body(response);
		}

		UserEntity user = new UserEntity(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()));
		user = userService.create(user, registerRequest);
		cartService.createCart(user);
		response.setStatus("200");
		response.setMessages(new ArrayList<String>(){{
			add("User registered successfully!");
		}});
		response.setEntity(null);
		return ResponseEntity.ok(response);
	}

}
