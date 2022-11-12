package com.ecommerce.DTO.auth;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
     
    @NotBlank
    @Size(min = 5, max = 25)
    private String username;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    private Set<String> roles;
}
