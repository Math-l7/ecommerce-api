package com.matheusluizroza.ecommerce_api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInputDTO {
    
    private String email;
    private String password;
}
