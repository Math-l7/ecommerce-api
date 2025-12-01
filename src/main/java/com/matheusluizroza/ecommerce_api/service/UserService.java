package com.matheusluizroza.ecommerce_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.matheusluizroza.ecommerce_api.dto.user.LoginReturn;
import com.matheusluizroza.ecommerce_api.dto.user.UserInputDTO;
import com.matheusluizroza.ecommerce_api.dto.user.UserReturn;
import com.matheusluizroza.ecommerce_api.enums.RoleName;
import com.matheusluizroza.ecommerce_api.model.Role;
import com.matheusluizroza.ecommerce_api.model.User;
import com.matheusluizroza.ecommerce_api.repository.RoleRepository;
import com.matheusluizroza.ecommerce_api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder bcrypt;

    private final JwtService jwt;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    public UserReturn dtoConvert(User user) {
        return new UserReturn(user.getId(), user.getName(), user.getEmail(), user.getRole().getRole().name());
    }

    @Transactional
    public UserReturn saveUser(String name, String email, String password) {

        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já cadastrado.");
        }
        Role role = roleRepository.findByRole(RoleName.CLIENTE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role não encontrada ."));

        User user = new User(name, email, bcrypt.encode(password));
        user.setRole(role);
        User userToSave = repository.save(user);
        return dtoConvert(userToSave);

    }

    public UserReturn findById(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID inválido."));

        return dtoConvert(user);
    }

    public UserReturn findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email inválido"));

        return dtoConvert(user);
    }

    public LoginReturn login(String email, String senha) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));

        User user = repository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não identificado"));
        String token = jwt.generateToken(email);

        return new LoginReturn(token, dtoConvert(user));
    }

    @Transactional
    public UserReturn updateUser(Integer idUser, UserInputDTO newUser) {

        User user = repository.findById(idUser)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id inválido."));
        if (!user.getEmail().equals(newUser.getEmail()) && repository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já em uso.");
        }

        user.setEmail(newUser.getEmail());
        user.setName(newUser.getName());

        User userToSave = repository.save(user);
        return dtoConvert(userToSave);

    }

    @Transactional
    public UserReturn changePassword(Integer id, String newPassword, String olderPassword) {

        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id inválido."));

        if (!bcrypt.matches(olderPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha incorreta.");
        }

        if (bcrypt.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Sua senha deve ser diferente da última utilizada.");
        }

        user.setPassword(bcrypt.encode(newPassword));
        User userToSave = repository.save(user);
        return dtoConvert(userToSave);

    }

    @Transactional
    public UserReturn changeUserRole(Integer id, RoleName roleName) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        Role roleSet = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role inválida."));

        user.setRole(roleSet);
        repository.save(user);
        return dtoConvert(user);

    }

    @Transactional
    public UserReturn deleteUser(Integer id) {
        User userDelete = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        repository.delete(userDelete);
        return dtoConvert(userDelete);
    }

    public List<UserReturn> getAllUsers() {
        return repository.findAll().stream()
                .map(list -> dtoConvert(list))
                .toList();

    }

    public List<UserReturn> getUsersByRole(RoleName roleName) {
        return repository.findAllUsersByRole(roleName).stream()
                .map(list -> dtoConvert(list))
                .toList();
    }
}
