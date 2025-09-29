package com.example.enterprise.gestorproducto.service.adapter;

import com.example.enterprise.gestorproducto.dto.ProductPriceDTO;
import com.example.enterprise.gestorproducto.exception.ExternalApiException;
import com.example.enterprise.gestorproducto.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyAdapter {

    private final WebClient webClient;

    @Value("${external.currency.url}")
    private String currencyApiUrl;

    @Value("${external.currency.api-key}")
    private String apiKey;

    @Value("${external.currency.base}")
    private String baseCurrency;

    public Mono<ProductPriceDTO> convertPrice(Product product, String targetCurrency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.fastforex.io")
                        .path("/fetch-one")
                        .queryParam("from", baseCurrency)
                        .queryParam("to", targetCurrency)
                        .queryParam("api_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorMap(e -> new ExternalApiException("Currency API not responding", e))
                .map(response -> {
                    try {
                        Map<String, Object> result = (Map<String, Object>) response.get("result");
                        if (result == null || !result.containsKey(targetCurrency)) {
                            throw new ExternalApiException("Currency not found in response", null);
                        }

                        BigDecimal rate = new BigDecimal(result.get(targetCurrency).toString());

                        return ProductPriceDTO.builder()
                                .productName(product.getName())
                                .originalPrice(product.getPrice())
                                .convertedPrice(product.getPrice().multiply(rate))
                                .currency(targetCurrency)
                                .build();
                    } catch (Exception e) {
                        throw new ExternalApiException("Error parsing currency API response", e);
                    }
                });
    }
}
