package com.matheusluizroza.ecommerce_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.matheusluizroza.ecommerce_api.dto.order.OrderInputDTO;
import com.matheusluizroza.ecommerce_api.dto.order.OrderItemDTO;
import com.matheusluizroza.ecommerce_api.dto.order.OrderReturnDTO;
import com.matheusluizroza.ecommerce_api.enums.OrderStatus;
import com.matheusluizroza.ecommerce_api.model.Order;
import com.matheusluizroza.ecommerce_api.model.OrderItem;
import com.matheusluizroza.ecommerce_api.model.Product;
import com.matheusluizroza.ecommerce_api.model.User;
import com.matheusluizroza.ecommerce_api.repository.OrderRepository;
import com.matheusluizroza.ecommerce_api.repository.ProductRepository;
import com.matheusluizroza.ecommerce_api.repository.UserRepository;
import com.matheusluizroza.ecommerce_api.service.OrderService;
import com.matheusluizroza.ecommerce_api.service.PaymentGateWayService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productrepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentGateWayService gatewayService;

    private User userEntity;
    private Product productEntity;
    private OrderItemDTO orderItemDTO;
    private OrderInputDTO orderInput;
    private Order orderEntity;

    @BeforeEach
    public void before() {
        // USER
        userEntity = new User();
        userEntity.setId(1);
        userEntity.setName("Matheus");

        // PRODUCT
        productEntity = new Product();
        productEntity.setId(1);
        productEntity.setName("Produto Teste");
        productEntity.setPrice(new BigDecimal("50"));
        productEntity.setStock(100);

        // OrderInputDTO
        orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(productEntity.getId());
        orderItemDTO.setQuantity(2);
        orderItemDTO.setPrice(productEntity.getPrice());

        orderInput = new OrderInputDTO();
        orderInput.setUserId(userEntity.getId());
        List<OrderItemDTO> items = new ArrayList<>();
        items.add(orderItemDTO);
        orderInput.setItems(items);

        // ORDER
        orderEntity = new Order();
        orderEntity.setId(1);
        orderEntity.setUser(userEntity);
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setTotal(productEntity.getPrice().multiply(BigDecimal.valueOf(orderItemDTO.getQuantity())));
        orderEntity.setItems(new ArrayList<>());

        // OrderItem
        OrderItem orderItem = new OrderItem(orderEntity, productEntity, 2);
        orderEntity.getItems().add(orderItem);
    }

    @Test
    public void createOrder_WithSucces() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(productrepository.findById(1)).thenReturn(Optional.of(productEntity));

        OrderReturnDTO orderFinal = service.createOrder(orderInput);

        assertEquals(userEntity.getName(), orderFinal.getUser());
        assertEquals(OrderStatus.PENDING, orderFinal.getStatus());
        assertEquals(1, orderFinal.getItems().size());

    }

    @Test
    public void createOrder_WhenUserIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.createOrder(orderInput));

        assertEquals("ID inválido.", exceptionTest.getReason());
    }

    @Test
    public void createOrder_WhenProductIdNotFound() {
        when(productrepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.createOrder(orderInput));

        assertEquals("ID inválido.", exceptionTest.getReason());
    }

    @Test
    public void createOrder_WhenDontHaveStock() {

        productEntity.setStock(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        when(productrepository.findById(1)).thenReturn(Optional.of(productEntity));

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.createOrder(orderInput));

        assertEquals("Estoque insuficiente para " + productEntity.getName(), exceptionTest.getReason());

    }

    @Test
    public void payOrder_WithSucces() {

        when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));
        when(gatewayService.processPayment(orderEntity)).thenReturn(true);

        OrderReturnDTO orderFinal = service.payOrder(1);

        assertEquals(OrderStatus.PAID, orderFinal.getStatus());

    }

    @Test
    public void payOrder_WhenStatusIsntPending() {
        orderEntity.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class, () -> service.payOrder(1));

        assertEquals("Pedido ainda não pode ser pago.", exceptionTest.getReason());
    }

    @Test
    public void cancelOrder_WithSucces() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(orderEntity));

        OrderReturnDTO orderFinal = service.cancelOrder(1);

        assertEquals(OrderStatus.CANCELED, orderFinal.getStatus());
    }

    @Test
    public void cancelOrder_WhenOrderNotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.cancelOrder(1));

        assertEquals("ID inválido.", exceptionTest.getReason());
    }

}
