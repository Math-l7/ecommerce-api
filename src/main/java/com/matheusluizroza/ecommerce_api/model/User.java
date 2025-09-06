package com.matheusluizroza.ecommerce_api.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }

    // one to many (user -> pedidos)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orderList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.password; // já existe atributo password
    }

    @Override
    public String getUsername() {
        return this.email; // Spring usa como "username", mas aqui vai ser o email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // se quiser bloquear usuário por expiração, muda aqui
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // se quiser bloquear manualmente usuários
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // se quiser expirar senha após X tempo
    }

    @Override
    public boolean isEnabled() {
        return true; // pode criar campo "enabled" no User e usar aqui
    }

}
