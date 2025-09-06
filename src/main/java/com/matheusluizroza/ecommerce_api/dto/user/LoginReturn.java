package com.matheusluizroza.ecommerce_api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginReturn {

    String token;
    UserReturn user;

    public LoginReturn(String token, UserReturn user) {
        this.token = token;
        this.user = user;
    }
}
