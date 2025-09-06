package com.matheusluizroza.ecommerce_api.dto.user;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReturn {

    private Integer id;
    private String name;
    private String email;
    private String roleName; // só o nome da role

    public UserReturn(Integer id, String name, String email, String roleName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roleName = roleName;
    }
}


