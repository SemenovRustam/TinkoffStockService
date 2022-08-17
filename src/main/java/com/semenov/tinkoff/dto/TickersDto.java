package com.semenov.tinkoff.dto;

import lombok.Data;

import java.util.List;

@Data
public class TickersDto {
    private List<String> tickers;
}
