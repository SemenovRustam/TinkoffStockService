package com.semenov.tinkoff.service;

import com.semenov.tinkoff.dto.FigiesDto;
import com.semenov.tinkoff.dto.StockPriceDto;
import com.semenov.tinkoff.dto.StocksDto;
import com.semenov.tinkoff.dto.TickersDto;
import com.semenov.tinkoff.exception.TinkoffException;
import com.semenov.tinkoff.model.Currency;
import com.semenov.tinkoff.model.Stock;
import com.semenov.tinkoff.service.abst.MarketService;
import com.semenov.tinkoff.service.abst.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TinkoffStockService implements StockService {
    private final OpenApi openApi;
    private final MarketService marketService;

    @Override
    public StocksDto getStocksByTicker(TickersDto tickersDto) {
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickersDto.getTickers()
                .forEach(ticker -> marketInstruments
                        .add(marketService.getMarketInstrumentListCompletableFuture(ticker))
                );

        List<Stock> stocks = marketInstruments.stream()
                .map(CompletableFuture::join)
                .map(marketInstrumentList -> {
                    if (marketInstrumentList.getInstruments().isEmpty()) {
                        return null;
                    }
                    return marketInstrumentList.getInstruments().get(0);
                })
                .filter(Objects::nonNull)
                .distinct()
                .map(this::getStock)
                .collect(Collectors.toList());

        return getStocksDto(stocks);
    }

    @Override
    public StockPriceDto getStockPriceByFigi(String figi) {
        Orderbook orderBook = openApi.getMarketContext()
                .getMarketOrderbook(figi, 0).join()
                .orElseThrow(() -> new TinkoffException("Order book not found"));

        BigDecimal lastPrice = orderBook.getLastPrice();
        log.info("Get price {}", figi);
        return getStockPriceDto(figi, lastPrice);
    }

    @Override
    public List<StockPriceDto> getStocksPrices(FigiesDto figiesDto) {
        List<CompletableFuture<Optional<Orderbook>>> orderBookListCF = new ArrayList<>();
        figiesDto.getFigies()
                .forEach(figi -> orderBookListCF
                        .add(marketService.getOrderBookByFigi(figi))
                );
        return orderBookListCF.stream()
                .map(CompletableFuture::join)
                .map(Optional::orElseThrow)
                .map(orderBook -> getStockPriceDto(orderBook.getFigi(), orderBook.getLastPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public Stock getStockByTicker(String ticker) {
        CompletableFuture<MarketInstrumentList> completableFuture = marketService.getMarketInstrumentListCompletableFuture(ticker);
        List<MarketInstrument> instruments = completableFuture.join().getInstruments();

        if (instruments.isEmpty()) {
            throw new TinkoffException(String.format("Stock %s not found.", ticker));
        }

        MarketInstrument stock = instruments.get(0);
        stock.getFigi();
        log.info("stock : {} ", stock);
        return getStock(stock);
    }


    private Stock getStock(MarketInstrument stock) {
        return Stock.builder()
                .ticker(stock.getTicker())
                .name(stock.getName())
                .type(stock.getType().getValue())
                .currency(Currency.valueOf(stock.getCurrency().getValue()))
                .figi(stock.getFigi())
                .source("TINKOFF")
                .isin(stock.getIsin())
                .price(getLastPrice(stock))
                .build();
    }

    private BigDecimal getLastPrice(MarketInstrument stock) {
        return marketService.getOrderBookByFigi(stock.getFigi())
                .join()
                .orElseThrow()
                .getLastPrice();
    }

    private StocksDto getStocksDto(List<Stock> stocks) {
        return StocksDto.builder()
                .stocks(stocks)
                .build();
    }

    private StockPriceDto getStockPriceDto(String figi, BigDecimal lastPrice) {
        return StockPriceDto.builder()
                .figi(figi)
                .price(lastPrice)
                .build();
    }
}
