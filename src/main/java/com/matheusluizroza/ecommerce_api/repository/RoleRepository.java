package com.matheusluizroza.ecommerce_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusluizroza.ecommerce_api.enums.RoleName;
import com.matheusluizroza.ecommerce_api.model.Role;

public interface RoleRepository extends JpaRepository <Role, Integer>{

    public Optional<Role> findByRole(RoleName role);
    
    public boolean existsByRole(RoleName role);
}
