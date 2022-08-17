package com.semenov.tinkoff.service.abst;

import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface MarketService {
    CompletableFuture<MarketInstrumentList> getMarketInstrumentListCompletableFuture(String ticker);
    CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi);

}
