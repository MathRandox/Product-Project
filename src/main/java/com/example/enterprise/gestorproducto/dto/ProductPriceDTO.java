package com.example.enterprise.gestorproducto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceDTO {
    private String productName;
    private BigDecimal originalPrice;
    private BigDecimal convertedPrice;
    private String currency;
}
