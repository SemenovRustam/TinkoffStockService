package com.semenov.tinkoff.service;

import com.semenov.tinkoff.service.abst.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.MarketContext;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkerServiceImpl implements MarketService {

    private final OpenApi openApi;

    @Async
    @Override
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentListCompletableFuture(String ticker) {
        MarketContext marketContext = openApi.getMarketContext();
        return marketContext.searchMarketInstrumentsByTicker(ticker);
    }

    @Async
    @Override
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi) {
        CompletableFuture<Optional<Orderbook>> marketOrderbook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("market Order book : {}", marketOrderbook);
        return marketOrderbook;
    }
}
