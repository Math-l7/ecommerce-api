package com.matheusluizroza.ecommerce_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matheusluizroza.ecommerce_api.dto.order.OrderInputDTO;
import com.matheusluizroza.ecommerce_api.dto.order.OrderReturnDTO;
import com.matheusluizroza.ecommerce_api.enums.OrderStatus;
import com.matheusluizroza.ecommerce_api.service.OrderService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderReturnDTO> createOrder(@RequestBody OrderInputDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(order));
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderReturnDTO> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderReturnDTO>> getOrderByUserId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getOrdersByUserId(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/by-status")
    public ResponseEntity<List<OrderReturnDTO>> getOrdersByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.ok(service.getOrdersByStatus(status));
    }

    @PutMapping("pay/{id}")
    public ResponseEntity<OrderReturnDTO> payOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(service.payOrder(id));
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<OrderReturnDTO> cancelOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cancelOrder(id));
    }

}
