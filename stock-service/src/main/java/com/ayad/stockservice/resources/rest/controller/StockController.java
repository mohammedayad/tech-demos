package com.ayad.stockservice.resources.rest.controller;


import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.dtos.StockPriceDto;
import com.ayad.stockservice.domain.model.entities.Stock;
import com.ayad.stockservice.domain.service.ifc.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/stock-service/v1/api")
@Slf4j
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }


    /**
     * Retrieves a paginated list of all stocks.
     *
     * @param page The page number (zero-based) of the results to retrieve.
     * @param size The number of stocks per page.
     * @return A {@link Page} containing the list of stocks for the specified page and size.
     * @throws IllegalArgumentException If the page number is less than zero.
     * @throws IllegalArgumentException If the size is less than one.
     */
    @GetMapping("/stocks")
    public ResponseEntity<Page<StockDto>> getAllStocks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("getAllStocks page no {} and the total size is {}", page, size);
        return new ResponseEntity<>(stockService.getAllStocks(page, size), HttpStatus.OK);

    }


    /**
     * Create a new stock record in the database.
     *
     * @param stock The stock object to be created. It should contain the following properties:
     *              - name (String): The name of the stock (required, maximum length: 50 characters).
     *              - current_price (Decimal): The current price of the stock (required, maximum precision: 10, scale: 2).
     * @return A ResponseEntity containing the created stock record along with the HTTP status code 201 (Created) if successful.
     * If the request body is invalid, it will respond with HTTP status code 400 (Bad Request).
     * If there is an internal server error during the stock creation process, it will respond with HTTP status code 500 (Internal Server Error).
     */
    @PostMapping("/stocks")
    public ResponseEntity<StockDto> createStock(@Valid @RequestBody StockDto stock) {
        log.info("add new Stock {}", stock);
        return new ResponseEntity<>(stockService.createStock(stock), HttpStatus.CREATED);

    }

    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockDto> getSingleStock(@PathVariable long id) {
        log.info("retrieve Stock with ID {}", id);
        return new ResponseEntity<>(stockService.getStockById(id), HttpStatus.OK);
    }

    @PatchMapping("/stocks/{id}")
    public ResponseEntity<StockDto> updateStockPrice(@PathVariable long id, @Valid @RequestBody StockPriceDto newPrice) {
        log.info("update Stock price with ID {}", id);
        return new ResponseEntity<>(stockService.updateStockPrice(id, newPrice.getCurrentPrice()), HttpStatus.OK);

    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable long id) {
        log.info("Delete stock with ID {}", id);
        stockService.deleteStockById(id);
        return ResponseEntity.noContent().build();

    }
}
