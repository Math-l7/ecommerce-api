package com.matheusluizroza.ecommerce_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.matheusluizroza.ecommerce_api.dto.user.LoginInputDTO;
import com.matheusluizroza.ecommerce_api.dto.user.LoginReturn;
import com.matheusluizroza.ecommerce_api.dto.user.UserInputDTO;
import com.matheusluizroza.ecommerce_api.dto.user.UserReturn;
import com.matheusluizroza.ecommerce_api.enums.RoleName;
import com.matheusluizroza.ecommerce_api.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    @PostMapping("/save-user")
    public ResponseEntity<UserReturn> saveUser(@RequestBody UserInputDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveUser(user.getName(), user.getEmail(), user.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginReturn> login(@RequestBody LoginInputDTO user) {
        return ResponseEntity.ok(service.login(user.getEmail(), user.getPassword()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserReturn> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/by-email")
    public ResponseEntity<UserReturn> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("all-users")
    public ResponseEntity<List<UserReturn>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("users-role")
    public ResponseEntity<List<UserReturn>> getUsersByRole(@RequestParam RoleName roleName) {
        return ResponseEntity.ok(service.getUsersByRole(roleName));
    }

    @PutMapping("update-user/{id}")
    public ResponseEntity<UserReturn> updateUser(@PathVariable Integer id, @RequestBody UserInputDTO userDTO) {
        return ResponseEntity.ok(service.updateUser(id, userDTO));
    }

    @PutMapping("change-password/{id}")
    public ResponseEntity<UserReturn> changePassword(@PathVariable Integer id, @RequestParam String newPassword,
            @RequestParam String olderPassword) {
        return ResponseEntity.ok(service.changePassword(id, newPassword, olderPassword));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("change-role/{id}")
    public ResponseEntity<UserReturn> changeUserRole(@PathVariable Integer id, @RequestBody RoleName roleName) {
        return ResponseEntity.ok(service.changeUserRole(id, roleName));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserReturn> deleteUser(@PathVariable Integer id) {
        return ResponseEntity.ok(service.deleteUser(id));
    }

}
