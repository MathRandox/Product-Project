package com.example.enterprise.gestorproducto.service;

import com.example.enterprise.gestorproducto.dto.ProductRequestDTO;
import com.example.enterprise.gestorproducto.model.Product;
import com.example.enterprise.gestorproducto.repository.ProductRepository;
import com.example.enterprise.gestorproducto.service.adapter.CurrencyAdapter;
import org.junit.jupiter.api.*;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

public class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private CurrencyAdapter currencyAdapter;

    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ProductService(repository, currencyAdapter);
    }

    @Test
    void createProduct_ShouldReturnProduct_WhenValidInput() {
        ProductRequestDTO request = ProductRequestDTO.builder()
                .name("Laptop")
                .price(BigDecimal.valueOf(1000))
                .stock(10)
                .build();

        Product saved = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(1000))
                .stock(10)
                .build();

        when(repository.save(any(Product.class))).thenReturn(Mono.just(saved));

        // Act
        Mono<?> result = service.createProduct(request);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(r -> ((com.example.enterprise.gestorproducto.dto.ProductResponseDTO) r).getName().equals("Laptop"))
                .verifyComplete();

        verify(repository, times(1)).save(any(Product.class));
    }
}
