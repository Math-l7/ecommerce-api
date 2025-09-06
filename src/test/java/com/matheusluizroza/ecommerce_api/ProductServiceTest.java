package com.matheusluizroza.ecommerce_api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.matheusluizroza.ecommerce_api.dto.product.ProductInputDTO;
import com.matheusluizroza.ecommerce_api.dto.product.ProductReturnDTO;
import com.matheusluizroza.ecommerce_api.model.OrderItem;
import com.matheusluizroza.ecommerce_api.model.Product;
import com.matheusluizroza.ecommerce_api.repository.ProductRepository;
import com.matheusluizroza.ecommerce_api.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private ProductInputDTO productInput;

    private Product productEntity;

    @BeforeEach
    public void before() {

        productInput = new ProductInputDTO();
        productInput.setName("productTest");
        productInput.setDescription("description");
        productInput.setPrice(BigDecimal.valueOf(20.00));
        productInput.setStock(5);

        productEntity = new Product("productTest", "description", BigDecimal.valueOf(20.00), 5);
        productEntity.setId(2);

    }

    @Test
    public void saveProduct_whenProductIsValid() {

        when(repository.existsByName("productTest")).thenReturn(false);
        when(repository.save(any(Product.class))).thenReturn(productEntity);

        ProductReturnDTO productFinal = service.saveProduct(productInput);

        assertEquals("productTest", productFinal.getName());
        assertEquals("description", productFinal.getDescription());
        assertEquals(BigDecimal.valueOf(20.00), productFinal.getPrice());
        assertEquals(5, productFinal.getStock());
    }

    @Test
    public void saveProduct_whenProductIsInvalid() {
        ProductInputDTO productTest = new ProductInputDTO();
        productTest.setName("productTest");

        when(repository.existsByName(anyString())).thenReturn(true);

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.saveProduct(productTest));

        assertEquals("Produto já cadastrado.", exceptionTest.getReason());

    }

    @Test
    public void getProductById_WhenIsFound() {

        when(repository.findById(2)).thenReturn(Optional.of(productEntity));

        ProductReturnDTO productFinal = service.getProductById(2);

        assertEquals("productTest", productFinal.getName());
        assertEquals("description", productFinal.getDescription());
        assertEquals(BigDecimal.valueOf(20.00), productFinal.getPrice());
        assertEquals(5, productFinal.getStock());
    }

    @Test
    public void getProductById_WhenNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.getProductById(2));
        assertEquals("ID inválido.", exceptionTest.getReason());
    }

    @Test
    public void orderValidation_WhenStockIsOk() {

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productEntity);
        orderItem.setQuantity(3);

        when(repository.findById(2)).thenReturn(Optional.of(productEntity));

        assertDoesNotThrow(() -> service.orderValidation(List.of(orderItem)));
        assertEquals(2, productEntity.getStock());
    }

    @Test
    public void orderValidation_WhenExcessedStock() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(productEntity);
        orderItem.setQuantity(10);

        when(repository.findById(2)).thenReturn(Optional.of(productEntity));

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.orderValidation(List.of(orderItem)));

        assertEquals("Estoque insuficiente para: " + productEntity.getName(), exceptionTest.getReason());
    }

    @Test
    public void updateProduct_WhenIsFound() {

        when(repository.findById(2)).thenReturn(Optional.of(productEntity));

        ProductInputDTO newProduct = new ProductInputDTO();
        newProduct.setName("new name Product");
        newProduct.setDescription("description");
        newProduct.setPrice(BigDecimal.valueOf(20.00));
        newProduct.setStock(10);

        ProductReturnDTO productResult = service.updateProduct(2, newProduct);

        assertEquals("new name Product", productResult.getName());
        assertEquals(10, productResult.getStock());

    }

    @Test
    public void updateProduct_WhenNotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        ResponseStatusException exceptionTest = assertThrows(ResponseStatusException.class,
                () -> service.updateProduct(2, new ProductInputDTO()));
        assertEquals("ID inválido.", exceptionTest.getReason());

    }

}
