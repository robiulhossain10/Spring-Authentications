package com.abc.authlogintest.service;

import com.abc.authlogintest.config.ResourceNotFoundException;
import com.abc.authlogintest.dtos.ProductDTO;
import com.abc.authlogintest.entity.Product;
import com.abc.authlogintest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // Get all products as DTO
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getImageUrl()
                ))
                .collect(Collectors.toList());
    }

    // Get product by ID
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }


    // Delete product by ID
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Save a product
    public ProductDTO saveProduct(ProductDTO dto) {
        Product product = new Product(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getImageUrl()
        );
        Product saved = productRepository.save(product);
        return new ProductDTO(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), saved.getImageUrl());
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        // Existing product fetch
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        // Update fields
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setImageUrl(dto.getImageUrl());

        // Save updated product
        Product saved = productRepository.save(existing);

        // Return DTO
        return new ProductDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getImageUrl()
        );
    }

}
