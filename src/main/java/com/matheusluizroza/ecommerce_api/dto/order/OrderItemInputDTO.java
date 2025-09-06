package com.matheusluizroza.ecommerce_api.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInputDTO {
    private Integer productId;
    private Integer quantity;
}
