package com.matheusluizroza.ecommerce_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusluizroza.ecommerce_api.enums.OrderStatus;
import com.matheusluizroza.ecommerce_api.model.Order;
import com.matheusluizroza.ecommerce_api.model.User;

public interface OrderRepository extends JpaRepository <Order, Integer>{
    
    public List<Order> findAllByUser(User user);

    List<Order> findByStatus(OrderStatus status);
}
