package com.semenov.tinkoff.service.abst;

import com.semenov.tinkoff.dto.FigiesDto;
import com.semenov.tinkoff.dto.StockPriceDto;
import com.semenov.tinkoff.dto.StocksDto;
import com.semenov.tinkoff.dto.TickersDto;
import com.semenov.tinkoff.model.Stock;

import java.util.List;

public interface StockService {
    Stock getStockByTicker(String ticker);

    StocksDto getStocksByTicker(TickersDto tickersDto);

    StockPriceDto getStockPriceByFigi(String figi);

    List<StockPriceDto> getStocksPrices(FigiesDto figiesDto);
}
