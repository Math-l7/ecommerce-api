package com.matheusluizroza.ecommerce_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.matheusluizroza.ecommerce_api.dto.product.ProductInputDTO;
import com.matheusluizroza.ecommerce_api.dto.product.ProductReturnDTO;
import com.matheusluizroza.ecommerce_api.model.OrderItem;
import com.matheusluizroza.ecommerce_api.model.Product;
import com.matheusluizroza.ecommerce_api.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public ProductReturnDTO toDTO(Product product) {
        ProductReturnDTO dto = new ProductReturnDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        return dto;
    }

    @Transactional
    public ProductReturnDTO saveProduct(ProductInputDTO product) {
        if (repository.existsByName(product.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto já cadastrado.");
        }
        Product productEntity = new Product(product.getName(), product.getDescription(), product.getPrice(),
                product.getStock());
        repository.save(productEntity);
        return toDTO(productEntity);
    }

    public ProductReturnDTO getProductById(Integer id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));
        return toDTO(product);
    }

    public List<ProductReturnDTO> getAllProducts() {
        return repository.findAll().stream().map(list -> toDTO(list)).toList();
    }

    public List<ProductReturnDTO> searchProducts(String productName) {
        return repository.findAll()
                .stream()
                .filter(l -> l.getName().toLowerCase().contains(productName.toLowerCase()))
                .map(list -> toDTO(list)).toList();
    }

    public void orderValidation(List<OrderItem> items) {

        for (OrderItem orderItem : items) {
            Product product = repository.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

            if (orderItem.getQuantity() > product.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Estoque insuficiente para: " + product.getName());
            }
            decreaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
        }
    }

    @Transactional
    public ProductReturnDTO updateProduct(Integer id, ProductInputDTO newProduct) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        product.setDescription(newProduct.getDescription());
        product.setName(newProduct.getName());
        product.setPrice(newProduct.getPrice());
        product.setStock(newProduct.getStock());

        repository.save(product);
        return toDTO(product);
    }

    @Transactional
    public ProductReturnDTO increaseStock(Integer id, Integer moreStock) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        Integer sum = product.getStock() + moreStock;
        product.setStock(sum);
        repository.save(product);
        return toDTO(product);
    }

    @Transactional
    public ProductReturnDTO decreaseStock(Integer id, Integer lessStock) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        if (lessStock > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque indisponível no momento.");
        }

        product.setStock(product.getStock() - lessStock);
        repository.save(product);
        return toDTO(product);
    }

    @Transactional
    public ProductReturnDTO deleteProduct(Integer id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID inválido."));

        repository.delete(product);
        return toDTO(product);
    }
}
