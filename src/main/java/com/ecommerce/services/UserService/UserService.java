package com.ecommerce.services.UserService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.DTO.auth.JwtResponseDTO;
import com.ecommerce.DTO.auth.LoginRequestDTO;
import com.ecommerce.DTO.auth.RegisterRequestDTO;
import com.ecommerce.models.RoleEntity;
import com.ecommerce.models.UserEntity;
import com.ecommerce.models.cart.CartEntity;
import com.ecommerce.models.enums.RoleTypes;
import com.ecommerce.repository.RoleRepo;
import com.ecommerce.repository.UserRepo;
import com.ecommerce.repository.cart.CartRepo;
import com.ecommerce.security.JwtUtils;


@Service
public class UserService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
	private CartRepo cartRepo;
    
    
    public UserService(UserRepo userRepo, RoleRepo roleRepo, AuthenticationManager authenticationManager,
            JwtUtils jwtUtils, CartRepo cartRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
		this.cartRepo = cartRepo;
    }

    public UserEntity create(UserEntity user, RegisterRequestDTO registerRequest ){
        Set<String> strRoles = registerRequest.getRoles();
		Set<RoleEntity> roles = new HashSet<>();
		if (strRoles == null) {
			RoleEntity userRole = roleRepo.findByName(RoleTypes.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
				RoleEntity adminRole = roleRepo.findByName(RoleTypes.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
				RoleEntity userRole = roleRepo.findByName(RoleTypes.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
        user.setCreatedAt(new Date());
        user.setUpdateAt(new Date());
		user = userRepo.save(user);

		CartEntity cart = new CartEntity();
		cart.setTotalItem((double) 0);
		cart.setTotalPrice(new BigDecimal(0));
		cart.setUser(user);
		cart.setCreatedAt(new Date());
		cart.setUpdateAt(new Date());

		cartRepo.save(cart);

		return user;
		
    }

    public Boolean existsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

	public Optional<UserEntity> findUserByJwtUsername(HttpServletRequest request){
		String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.parseJwt(request));
		return userRepo.findByUsername(username);
	}

    public JwtResponseDTO userIsAuthenticated(LoginRequestDTO loginRequest){
        Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return new JwtResponseDTO(token, userDetails.getId(), userDetails.getUsername(), roles);
    }
}
