package com.matheusluizroza.ecommerce_api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.matheusluizroza.ecommerce_api.model.Order;

@Service
public class PaymentGateWayService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String GATEWAY_URL = "https://httpbin.org/post";

    public boolean processPayment(Order order) {
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("orderId", order.getId());
        paymentData.put("amount", order.getTotal());
        paymentData.put("customerEmail", order.getUser().getEmail());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GATEWAY_URL, paymentData, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
