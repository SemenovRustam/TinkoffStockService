package com.semenov.tinkoff.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    CHF("CHF"),
    JPY("JPY"),
    CNY("CNY"),
    TRY("TRY");

    private String currency;
}
