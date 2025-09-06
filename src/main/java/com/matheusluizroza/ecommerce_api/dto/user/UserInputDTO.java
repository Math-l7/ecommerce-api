package com.matheusluizroza.ecommerce_api.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInputDTO {

    private Integer id;
    private String name;
    private String email;
    private String password;
    private String roleName;

    public UserInputDTO(Integer id, String name, String email, String roleName, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roleName = roleName;
        this.password = password;
    }
}
