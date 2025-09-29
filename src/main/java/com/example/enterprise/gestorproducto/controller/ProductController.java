package com.example.enterprise.gestorproducto.controller;

import com.example.enterprise.gestorproducto.dto.ApiResponseDTO;
import com.example.enterprise.gestorproducto.dto.ProductPriceDTO;
import com.example.enterprise.gestorproducto.dto.ProductRequestDTO;
import com.example.enterprise.gestorproducto.dto.ProductResponseDTO;
import com.example.enterprise.gestorproducto.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public Mono<ResponseEntity<ApiResponseDTO<ProductResponseDTO>>> create(@Valid @RequestBody ProductRequestDTO dto) {
        return service.createProduct(dto).map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<ProductResponseDTO> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductResponseDTO> getById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ApiResponseDTO<ProductResponseDTO>>> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        return service.updateProduct(id, dto).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ApiResponseDTO<Void>>> delete(@PathVariable Long id) {
        return service.deleteProduct(id).map(ResponseEntity::ok);
    }

    @GetMapping("/low-stock")
    public Flux<ProductResponseDTO> getLowStock() {
        return service.getProductsLowStock();
    }

    @GetMapping("/{id}/price")
    public Mono<ProductPriceDTO> getPriceById(
            @PathVariable Long id,
            @RequestParam String currency
    ) {
        return service.getProductPriceInCurrency(id, currency);
    }
}
