package com.matheusluizroza.ecommerce_api.dto.order;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    private Integer id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
