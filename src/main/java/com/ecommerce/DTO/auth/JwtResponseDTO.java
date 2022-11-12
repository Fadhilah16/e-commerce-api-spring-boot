package com.ecommerce.DTO.auth;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JwtResponseDTO {
    @NonNull
    private String token;
    private String type = "Bearer";
    @NonNull
    private Long id;
    @NonNull
    private String username;
    @NonNull
    private List<String> roles;
}
