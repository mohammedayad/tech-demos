package com.ayad.stockservice.resources.rest.controller;

import com.ayad.stockservice.common.exception.StockNotFoundException;
import com.ayad.stockservice.common.utils.StockUtility;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.dtos.StockPriceDto;
import com.ayad.stockservice.domain.model.entities.Stock;
import com.ayad.stockservice.domain.service.ifc.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockController stockController;

    private Stock stock1;
    private StockDto stockDto1;

    @BeforeEach
    public void setUp() {
        stock1 = new Stock();
        stock1.setId(1L);
        stock1.setName("Stock 1");
        stock1.setCurrentPrice(BigDecimal.valueOf(100.0));

        stockDto1 = new StockDto();
        stockDto1.setId(1L);
        stockDto1.setName("Stock 1");
        stockDto1.setCurrentPrice(BigDecimal.valueOf(100.0));
    }

    @Test
    public void testGetAllStocks_Success() {
        int page = 0;
        int size = 10;
        Page<StockDto> mockStocksPage = new PageImpl<>(List.of(stockDto1));

        when(stockService.getAllStocks(page, size)).thenReturn(mockStocksPage);

        ResponseEntity<Page<StockDto>> response = stockController.getAllStocks(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockStocksPage, response.getBody());
    }

    @Test
    public void testCreateStock_Success() {
        when(stockService.createStock(any())).thenReturn(stockDto1);

        ResponseEntity<StockDto> response = stockController.createStock(stockDto1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(stockDto1, response.getBody());
    }

    @Test
    public void testGetSingleStock_Success() throws StockNotFoundException {
        long id = 1L;
        when(stockService.getStockById(id)).thenReturn(stockDto1);

        ResponseEntity<StockDto> response = stockController.getSingleStock(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(stockDto1, response.getBody());
    }

    @Test
    public void testGetSingleStock_StockNotFound() throws StockNotFoundException {
        long id = 3L;
        try {
            when(stockService.getStockById(id)).thenThrow(new StockNotFoundException(HttpStatus.NOT_FOUND, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id)));
            stockController.getSingleStock(id);
            fail("Expected StockNotFoundException to be thrown");
        } catch (StockNotFoundException e) {
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(e.getErrorCode(), is(StockUtility.CONSTRAINT_VIOLATIONS));
            assertThat(e.getErrorMessage(), is(String.format(StockUtility.NO_Stock_MATCHING_FOUND, id)));
        }

    }

    @Test
    public void testUpdateStockPrice_Success() throws StockNotFoundException {
        long id = 1L;
        StockPriceDto newPrice = new StockPriceDto();
        newPrice.setCurrentPrice(BigDecimal.valueOf(200.0));
        when(stockService.updateStockPrice(id, newPrice.getCurrentPrice())).thenReturn(stockDto1);

        ResponseEntity<StockDto> response = stockController.updateStockPrice(id, newPrice);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(stockDto1, response.getBody());
    }

    @Test
    public void testUpdateStockPrice_StockNotFound() throws StockNotFoundException {
        long id = 3L;
        StockPriceDto newPrice = new StockPriceDto();
        newPrice.setCurrentPrice(BigDecimal.valueOf(200.0));
        when(stockService.updateStockPrice(id, newPrice.getCurrentPrice())).thenThrow(StockNotFoundException.class);

        assertThrows(StockNotFoundException.class, () -> stockController.updateStockPrice(id, newPrice));
    }

    @Test
    public void testDeleteStock_Success() throws StockNotFoundException {
        long id = 3L;

        ResponseEntity<Void> response = stockController.deleteStock(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(stockService, times(1)).deleteStockById(id);
    }

    @Test
    public void testDeleteStock_StockNotFound() throws StockNotFoundException {
        long id = 1L;
        doThrow(StockNotFoundException.class).when(stockService).deleteStockById(id);
        assertThrows(StockNotFoundException.class, () -> stockController.deleteStock(id));
    }
}
