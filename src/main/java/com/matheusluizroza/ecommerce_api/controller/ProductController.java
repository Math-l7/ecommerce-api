package com.matheusluizroza.ecommerce_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusluizroza.ecommerce_api.dto.product.ProductInputDTO;
import com.matheusluizroza.ecommerce_api.dto.product.ProductReturnDTO;
import com.matheusluizroza.ecommerce_api.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ProductReturnDTO> saveProduct(@RequestBody ProductInputDTO product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveProduct(product));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductReturnDTO> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductReturnDTO>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductReturnDTO>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(service.searchProducts(name));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductReturnDTO> updateProduct(@PathVariable Integer id,
            @RequestBody ProductInputDTO newProduct) {
        return ResponseEntity.ok(service.updateProduct(id, newProduct));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductReturnDTO> deleteProduct(@PathVariable Integer id) {
        return ResponseEntity.ok(service.deleteProduct(id));
    }

}
