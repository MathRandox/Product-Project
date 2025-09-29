package com.example.enterprise.gestorproducto.repository;

import com.example.enterprise.gestorproducto.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Flux<Product> findByStockLessThan(int stock);
}
