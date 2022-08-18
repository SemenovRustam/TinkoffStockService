package com.semenov.tinkoff.controller;

import com.semenov.tinkoff.dto.FigiesDto;
import com.semenov.tinkoff.dto.StockPriceDto;
import com.semenov.tinkoff.dto.StocksDto;
import com.semenov.tinkoff.dto.TickersDto;
import com.semenov.tinkoff.model.Stock;
import com.semenov.tinkoff.service.abst.StockService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public ResponseEntity<Stock> getStock(@PathVariable String ticker) {
        Stock stockByTicker = stockService.getStockByTicker(ticker);
        return new ResponseEntity<>(stockByTicker, HttpStatus.OK);
    }

    @PostMapping("/stocks/getAllStocks")
    @ApiOperation(value = "Получение списка акций", notes = "Введите список тикеров")
    public ResponseEntity<StocksDto> getStocksByTickers(@RequestBody TickersDto tickers) {
        StocksDto stocks = stockService.getStocksByTicker(tickers);
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @GetMapping("/stocks/getPrice/{figi}")
    @ApiOperation(value = "Получить цену акции", notes = "Введите FIGI акции, чтоб получить цену")
    public ResponseEntity<StockPriceDto> getStockPriceByFigi(@PathVariable String figi) {
        StockPriceDto stockPriceByFigi = stockService.getStockPriceByFigi(figi);
        return new ResponseEntity<>(stockPriceByFigi, HttpStatus.OK);
    }

    @PostMapping("/stocks/getPrices")
    @ApiOperation(value = "Получить цену акций", notes = "Введите список FIGI акции, чтоб получить цену")
    public ResponseEntity<List<StockPriceDto>> getStocksPricesByFigi(@RequestBody FigiesDto figies) {
        List<StockPriceDto> stocksPrices = stockService.getStocksPrices(figies);
        return new ResponseEntity<>(stocksPrices, HttpStatus.OK);
    }
}
