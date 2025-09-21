package com.abc.authlogintest.controller;

import com.abc.authlogintest.dtos.ProductDTO;
import com.abc.authlogintest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> findAllProducts(){
        return productService.getAllProducts();
    }

@GetMapping("/{id}")
    public ProductDTO findProductsById(@PathVariable Long id){
    return productService.getProductById(id);
}

@PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO){
        return productService.saveProduct(productDTO);
}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PutMapping("/{id}")
    public ProductDTO updateProducts(@PathVariable Long id,@RequestBody ProductDTO productDTO){
        return productService.updateProduct(id,productDTO);
    }
}
