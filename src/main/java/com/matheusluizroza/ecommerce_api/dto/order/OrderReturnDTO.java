package com.matheusluizroza.ecommerce_api.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.matheusluizroza.ecommerce_api.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderReturnDTO {
    private Integer orderId;
    private LocalDateTime data;
    private BigDecimal total;
    private String user;
    private OrderStatus status;
    private List<OrderItemDTO> items;
}
