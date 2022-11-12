package com.ecommerce.DTO;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO <T>{
    private String status;
    private List<String> messages = new ArrayList<>();
    private T entity;
    
}
