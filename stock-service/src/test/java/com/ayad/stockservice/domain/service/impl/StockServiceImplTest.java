package com.ayad.stockservice.domain.service.impl;

import com.ayad.stockservice.common.exception.StockNotFoundException;
import com.ayad.stockservice.common.utils.StockUtility;
import com.ayad.stockservice.domain.mapper.StockMapper;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.entities.Stock;
import com.ayad.stockservice.domain.repository.ifc.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    private Stock stock1;
    private Stock stock2;
    private StockDto stockDto1;
    private StockDto stockDto2;


    @BeforeEach
    public void setUp() {
        stock1 = new Stock();
        stock1.setId(1L);
        stock1.setName("Stock 1");
        stock1.setCurrentPrice(BigDecimal.valueOf(100.0));
        stock2 = new Stock();
        stock1.setId(2L);
        stock1.setName("Stock 2");
        stock1.setCurrentPrice(BigDecimal.valueOf(200.0));

        stockDto1 = new StockDto();
        stockDto1.setId(1L);
        stockDto1.setName("Stock 1");
        stockDto1.setCurrentPrice(BigDecimal.valueOf(100.0));
        stockDto2 = new StockDto();
        stockDto2.setId(2L);
        stockDto2.setName("Stock 2");
        stockDto2.setCurrentPrice(BigDecimal.valueOf(200.0));
    }


    @Test
    public void testGetAllStocks() {
        int page = 0;
        int size = 10;
        List<Stock> mockStockList = new ArrayList<>();
        mockStockList.add(stock1);
        mockStockList.add(stock2);

        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> mockPage = new PageImpl<>(mockStockList, pageable, mockStockList.size());

        when(stockRepository.findAll(pageable)).thenReturn(mockPage);
        when(stockMapper.stockToStockDto(any()))
                .thenAnswer(invocation -> {
                    Stock stock = invocation.getArgument(0);
                    StockDto stockDto = new StockDto();
                    stockDto.setId(stock.getId());
                    stockDto.setName(stock.getName());
                    stockDto.setCurrentPrice(stock.getCurrentPrice());
                    return stockDto;
                });


        Page<StockDto> resultPage = stockService.getAllStocks(page, size);


        assertEquals(mockStockList.size(), resultPage.getTotalElements());
        assertEquals(mockStockList.size(), resultPage.getContent().size());
        assertEquals(mockStockList.get(0).getId(), resultPage.getContent().get(0).getId());
        assertEquals(mockStockList.get(0).getName(), resultPage.getContent().get(0).getName());
        assertEquals(mockStockList.get(0).getCurrentPrice(), resultPage.getContent().get(0).getCurrentPrice());
    }

    @Test
    public void testCreateStock() {

        when(stockMapper.stockDtoToStock(stockDto1)).thenReturn(stock1);
        when(stockRepository.save(stock1)).thenReturn(stock1);
        when(stockMapper.stockToStockDto(stock1)).thenReturn(stockDto1);


        StockDto createdStockDto = stockService.createStock(stockDto1);


        assertEquals(stockDto1, createdStockDto);
    }

    @Test
    public void testGetStockById_Success() throws StockNotFoundException {
        long id = 1L;
        when(stockRepository.getReferenceById(id)).thenReturn(stock1);
        when(stockMapper.stockToStockDto(stock1)).thenReturn(stockDto1);

        StockDto resultStockDto = stockService.getStockById(id);

        assertEquals(stockDto1, resultStockDto);
    }

    @Test
    public void testGetStockById_StockNotFound() {
        long id = 3L;
        when(stockRepository.getReferenceById(id)).thenThrow(EntityNotFoundException.class);
        assertThrows(StockNotFoundException.class, () -> stockService.getStockById(id));
    }

    @Test
    public void testGetStockById_StockNotFound2() throws StockNotFoundException {
        long id = 3L;
        try {
            when(stockRepository.getReferenceById(id)).thenThrow(EntityNotFoundException.class);
            stockService.getStockById(id);
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
        BigDecimal newPrice = BigDecimal.valueOf(200.0);
        when(stockRepository.getReferenceById(id)).thenReturn(stock1);
        when(stockRepository.save(stock1)).thenReturn(stock1);
        when(stockMapper.stockToStockDto(stock1)).thenReturn(stockDto1);

        StockDto updatedStockDto = stockService.updateStockPrice(id, newPrice);

        assertEquals(newPrice, stock1.getCurrentPrice());
        assertEquals(stockDto1, updatedStockDto);
    }

    @Test
    public void testUpdateStockPrice_StockNotFound() {
        long id = 3L;
        BigDecimal newPrice = BigDecimal.valueOf(200.0);
        when(stockRepository.getReferenceById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(StockNotFoundException.class, () -> stockService.updateStockPrice(id, newPrice));
    }

    @Test
    public void testUpdateStockPrice_StockNotFound2() throws StockNotFoundException {
        long id = 3L;
        BigDecimal newPrice = BigDecimal.valueOf(200.0);
        try {
            doThrow(EntityNotFoundException.class).when(stockRepository).getReferenceById(id);
            stockService.updateStockPrice(id, newPrice);
            fail("Expected StockNotFoundException to be thrown");
        } catch (StockNotFoundException e) {
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(e.getErrorCode(), is(StockUtility.CONSTRAINT_VIOLATIONS));
            assertThat(e.getErrorMessage(), is(String.format(StockUtility.NO_Stock_MATCHING_FOUND, id)));
        }

    }


    @Test
    public void testDeleteStockById_Success() throws StockNotFoundException {
        long id = 1L;

        stockService.deleteStockById(id);

        verify(stockRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteStockById_StockNotFound() {
        long id = 3L;
        doThrow(EmptyResultDataAccessException.class).when(stockRepository).deleteById(id);

        assertThrows(StockNotFoundException.class, () -> stockService.deleteStockById(id));
    }

    @Test
    public void testDeleteStockById_StockNotFound2() throws StockNotFoundException {
        long id = 3L;
        try {
            doThrow(EmptyResultDataAccessException.class).when(stockRepository).deleteById(id);
            stockService.deleteStockById(id);
            fail("Expected StockNotFoundException to be thrown");
        } catch (StockNotFoundException e) {
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(e.getErrorCode(), is(StockUtility.CONSTRAINT_VIOLATIONS));
            assertThat(e.getErrorMessage(), is(String.format(StockUtility.NO_Stock_MATCHING_FOUND, id)));
        }

    }
}




