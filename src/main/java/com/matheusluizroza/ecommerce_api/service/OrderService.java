package com.matheusluizroza.ecommerce_api.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.matheusluizroza.ecommerce_api.dto.order.OrderItemDTO;
import com.matheusluizroza.ecommerce_api.dto.order.OrderReturnDTO;
import com.matheusluizroza.ecommerce_api.dto.order.OrderInputDTO;
import com.matheusluizroza.ecommerce_api.enums.OrderStatus;
import com.matheusluizroza.ecommerce_api.model.Order;
import com.matheusluizroza.ecommerce_api.model.OrderItem;
import com.matheusluizroza.ecommerce_api.model.Product;
import com.matheusluizroza.ecommerce_api.model.User;
import com.matheusluizroza.ecommerce_api.repository.OrderRepository;
import com.matheusluizroza.ecommerce_api.repository.ProductRepository;
import com.matheusluizroza.ecommerce_api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductService serviceProduct;

    private final ProductRepository productRepository;

    private final PaymentGateWayService gatewayService;

    public OrderReturnDTO toDTO(Order order) {
        OrderReturnDTO dto = new OrderReturnDTO();
        dto.setOrderId(order.getId());
        dto.setData(order.getData());
        dto.setTotal(order.getTotal());
        dto.setUser(order.getUser().getName());
        dto.setStatus(order.getStatus());

        List<OrderItemDTO> items = order.getItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setName(item.getProduct().getName());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getProduct().getPrice());
            return itemDTO;
        }).toList();

        dto.setItems(items);

        return dto;
    }

    @Transactional
    public OrderReturnDTO createOrder(OrderInputDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        List<OrderItem> list = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

            if (product.getStock() < itemDTO.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente para " + product.getName());
            }
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            serviceProduct.decreaseStock(product.getId(), itemDTO.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            list.add(orderItem);
        } //

        Order order = new Order();
        order.setData(LocalDateTime.now());
        order.setItems(list);
        order.setStatus(OrderStatus.PENDING);
        order.setTotal(total);
        order.setUser(user);

        orderRepository.save(order);
        return toDTO(order);
    }

    @Transactional
    public OrderReturnDTO payOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido ainda não pode ser pago.");
        }

        boolean payment = gatewayService.processPayment(order);

        if (payment) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }
        orderRepository.save(order);
        return toDTO(order);

    }

    public OrderReturnDTO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));
        return toDTO(order);
    }

    public List<OrderReturnDTO> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido"));

        List<Order> list = user.getOrderList();

        List<OrderReturnDTO> listReady = new ArrayList<>();

        for (Order order : list) {
            listReady.add(toDTO(order));
        }

        return listReady;

    }

    public List<OrderReturnDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> ordersList = orderRepository.findAll().stream()
                .filter(o -> o.getStatus().name().equals(status.name())).toList();

        List<OrderReturnDTO> list = new ArrayList<>();
        for (Order order : ordersList) {
            list.add(toDTO(order));
        }
        return list;
    }

    @Transactional
    public OrderReturnDTO cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        for (OrderItem item : order.getItems()) {
            serviceProduct.increaseStock(item.getProduct().getId(), item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return toDTO(order);
    }

}
