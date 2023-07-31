package com.ayad.stockservice.resources.rest.controller;


import com.ayad.stockservice.domain.model.dtos.ProblemDto;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.dtos.StockPriceDto;
import com.ayad.stockservice.domain.service.ifc.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/stock-service/v1/api")
@Tag(name = "Stocks APIs", description = "API for managing stocks")
@Slf4j
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }


    @GetMapping("/stocks")
    @Operation(summary = "Get all stocks", description = "Retrieves a paginated list of all stocks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns a page of stocks"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<Page<StockDto>> getAllStocks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("getAllStocks page no {} and the total size is {}", page, size);
        return new ResponseEntity<>(stockService.getAllStocks(page, size), HttpStatus.OK);

    }



    @PostMapping("/stocks")
    @Operation(summary = "Create a new stock record", description = "Create a new stock record in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation. Returns the created stock record"),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<StockDto> createStock(@Valid @RequestBody StockDto stock) {
        log.info("add new Stock {}", stock);
        return new ResponseEntity<>(stockService.createStock(stock), HttpStatus.CREATED);

    }

    @GetMapping("/stocks/{id}")
    @Operation(summary = "Get a single stock", description = "Retrieves a single stock by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns the requested stock record"),
            @ApiResponse(responseCode = "404", description = "Stock not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<StockDto> getSingleStock(@PathVariable long id) {
        log.info("retrieve Stock with ID {}", id);
        return new ResponseEntity<>(stockService.getStockById(id), HttpStatus.OK);
    }

    @PatchMapping("/stocks/{id}")
    @Operation(summary = "Update stock price", description = "Update the price of a stock by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation. Returns the updated stock record"),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "404", description = "Stock not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<StockDto> updateStockPrice(@PathVariable long id, @Valid @RequestBody StockPriceDto newPrice) {
        log.info("update Stock price with ID {}", id);
        return new ResponseEntity<>(stockService.updateStockPrice(id, newPrice.getCurrentPrice()), HttpStatus.OK);

    }

    @DeleteMapping("/stocks/{id}")
    @Operation(summary = "Delete a stock", description = "Deletes a single stock by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation. No content returned"),
            @ApiResponse(responseCode = "404", description = "Stock not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDto.class)))
    })
    public ResponseEntity<Void> deleteStock(@PathVariable long id) {
        log.info("Delete stock with ID {}", id);
        stockService.deleteStockById(id);
        return ResponseEntity.noContent().build();

    }
}
