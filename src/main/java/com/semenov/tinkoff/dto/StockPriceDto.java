package com.semenov.tinkoff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class StockPriceDto {
    private String figi;
    private BigDecimal price;
}
