package com.matheusluizroza.ecommerce_api.dto.order;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInputDTO {

    private Integer userId;
    private List<OrderItemDTO> items;
}
