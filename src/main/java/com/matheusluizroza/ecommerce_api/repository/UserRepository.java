package com.matheusluizroza.ecommerce_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusluizroza.ecommerce_api.enums.RoleName;
import com.matheusluizroza.ecommerce_api.model.User;

public interface UserRepository extends JpaRepository <User, Integer>{

    public Optional<User> findByEmail(String email);

    public boolean existsByEmail(String email);

    public List<User> findAllUsersByRole(RoleName roleName);

}
