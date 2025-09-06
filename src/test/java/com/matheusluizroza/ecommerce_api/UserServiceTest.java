package com.matheusluizroza.ecommerce_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.matheusluizroza.ecommerce_api.dto.user.LoginReturn;
import com.matheusluizroza.ecommerce_api.dto.user.UserInputDTO;
import com.matheusluizroza.ecommerce_api.dto.user.UserReturn;
import com.matheusluizroza.ecommerce_api.enums.RoleName;
import com.matheusluizroza.ecommerce_api.model.Role;
import com.matheusluizroza.ecommerce_api.model.User;
import com.matheusluizroza.ecommerce_api.repository.RoleRepository;
import com.matheusluizroza.ecommerce_api.repository.UserRepository;
import com.matheusluizroza.ecommerce_api.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private RoleRepository roleRepository;

    private User userEntity;

    private UserInputDTO userInput;

    private Role roleEntity;

    @BeforeEach
    public void before() {

        userEntity = new User("Matheus", "mathEmail", "140208");
        userEntity.setId(1);

        roleEntity = new Role();
        roleEntity.setRole(RoleName.CLIENTE);
        roleEntity.setId(1);

        userInput = new UserInputDTO();
        userInput.setName("Matheus");
        userInput.setEmail("mathEmail");
        userInput.setPassword("140208");
        userInput.setId(1);
        userEntity.setRole(roleEntity);

    }

    @Test
    public void saveUser_WithSucces() {

        when(repository.existsByEmail("mathEmail")).thenReturn(false);

        UserReturn userFinal = service.saveUser(userInput.getName(), userInput.getEmail(), userInput.getPassword());

        assertEquals("Matheus", userFinal.getName());
        assertEquals("mathEmail", userFinal.getEmail());
    }

    @Test
    public void saveUser_WhenEmailExists() {
        when(repository.existsByEmail("mathEmail")).thenReturn(true);

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.saveUser("Matheus", "mathEmail", "140208"));

        assertEquals("Usuário já cadastrado.", exceptionTest.getReason());
    }

    @Test
    public void findById_WithSucces() {
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));

        UserReturn userFinal = service.findById(userEntity.getId());

        assertEquals("Matheus", userFinal.getName());
        assertEquals("mathEmail", userFinal.getEmail());
    }

    @Test
    public void findById_WhenNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class, () -> service.findById(1));

        assertEquals("ID inválido.", exceptionTest.getReason());

    }

    @Test
    public void findByEmail_WithSucces() {
        when(repository.findByEmail("mathEmail")).thenReturn(Optional.of(userEntity));

        UserReturn userFinal = service.findByEmail(userEntity.getEmail());

        assertEquals("Matheus", userFinal.getName());
        assertEquals("mathEmail", userFinal.getEmail());
    }

    @Test
    public void findByEmail_WhenNotFound() {
        when(repository.findByEmail("mathEmail")).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.findByEmail("mathEmail"));

        assertEquals("Email inválido.", exceptionTest.getReason());
    }

    @Test
    public void login_WithSucces() {
        when(repository.findByEmail(userInput.getEmail())).thenReturn(Optional.of(userEntity));

        LoginReturn userFinal = service.login(userInput.getEmail(), userInput.getPassword());

        assertEquals("Matheus", userFinal.getUser().getName());
        assertEquals(1, userFinal.getUser().getId());
    }

    @Test
    public void login_WhenNotFound() {
        when(repository.findByEmail(userInput.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.login(userInput.getEmail(), userInput.getPassword()));

        assertEquals("Usuário não identificado", exceptionTest.getReason());
    }

    @Test
    public void updateUser_WithSucces() {
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserInputDTO newUser = new UserInputDTO();
        newUser.setName("Miguel");
        newUser.setEmail("miguel123");
        newUser.setPassword("140208");

        UserReturn userFinal = service.updateUser(1, newUser);

        assertEquals("miguel123", userFinal.getEmail());
        assertEquals("Miguel", userFinal.getName());
    }

    @Test
    public void updateUser_WhenEmailIsTheSame() {
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));
        when(repository.findByEmail("miguelEmail"))
                .thenReturn(Optional.of(new User("Miguel", "miguelEmail", "140208")));

        UserInputDTO newUser = new UserInputDTO();
        newUser.setName("miguelzinho");
        newUser.setEmail("miguelEmail");

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.updateUser(1, newUser));

        assertEquals("Email já em uso.", exceptionTest.getReason());

    }

    @Test
    public void changeUserRole_WithSucces() {
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByRole(RoleName.ADMIN)).thenReturn(Optional.of(roleEntity));

        RoleName roleName = RoleName.ADMIN;

        UserReturn userFinal = service.changeUserRole(1, roleName);

        assertEquals(roleName, userFinal.getRoleName());

    }

    @Test
    public void changeUserRole_WhenNotFoundRole() {
        when(repository.findById(1)).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByRole(RoleName.ADMIN)).thenReturn(Optional.empty());

        RoleName roleName = RoleName.ADMIN;

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.changeUserRole(1, roleName));

        assertEquals("Role inválida.", exceptionTest.getReason());
    }

}
