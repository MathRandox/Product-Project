package com.example.enterprise.gestorproducto.service;

import com.example.enterprise.gestorproducto.dto.ApiResponseDTO;
import com.example.enterprise.gestorproducto.dto.ProductPriceDTO;
import com.example.enterprise.gestorproducto.dto.ProductRequestDTO;
import com.example.enterprise.gestorproducto.dto.ProductResponseDTO;
import com.example.enterprise.gestorproducto.exception.BusinessException;
import com.example.enterprise.gestorproducto.exception.ResourceNotFoundException;
import com.example.enterprise.gestorproducto.model.Product;
import com.example.enterprise.gestorproducto.repository.ProductRepository;
import com.example.enterprise.gestorproducto.service.adapter.CurrencyAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CurrencyAdapter currencyAdapter;

    public Mono<ApiResponseDTO<ProductResponseDTO>> createProduct(ProductRequestDTO dto) {
        if (dto.getPrice().signum() <= 0) {
            return Mono.error(new BusinessException("Price must be positive"));
        }

        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();

        return repository.save(product)
                .map(saved -> ApiResponseDTO.<ProductResponseDTO>builder()
                        .message("Producto creado correctamente")
                        .data(toResponse(saved))
                        .build());
    }

    public Flux<ProductResponseDTO> getAllProducts() {
        return repository.findAll().map(this::toResponse);
    }

    public Mono<ProductResponseDTO> getProductById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found")))
                .map(this::toResponse);
    }

    public Mono<ApiResponseDTO<ProductResponseDTO>> updateProduct(Long id, ProductRequestDTO dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found")))
                .flatMap(existing -> {
                    existing.setName(dto.getName());
                    existing.setPrice(dto.getPrice());
                    existing.setStock(dto.getStock());
                    return repository.save(existing);
                })
                .map(updated -> ApiResponseDTO.<ProductResponseDTO>builder()
                        .message("Producto actualizado correctamente")
                        .data(toResponse(updated))
                        .build());
    }

    public Mono<ApiResponseDTO<Void>> deleteProduct(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found")))
                .flatMap(product -> repository.delete(product)
                        .then(Mono.just(ApiResponseDTO.<Void>builder()
                                .message("Producto eliminado correctamente")
                                .build())));
    }

    public Flux<ProductResponseDTO> getProductsLowStock() {
        return repository.findByStockLessThan(5).map(this::toResponse);
    }

    private ProductResponseDTO toResponse(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public Mono<ProductPriceDTO> getProductPriceInCurrency(Long id, String currency) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found")))
                .flatMap(product -> currencyAdapter.convertPrice(product, currency));
    }

}
