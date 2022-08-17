package com.semenov.tinkoff.dto;

import com.semenov.tinkoff.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class StocksDto {
    private List<Stock> stocks;
}
