package com.ayad.stockservice.resources.rest.controller;

import com.ayad.stockservice.common.utils.StockUtility;
import com.ayad.stockservice.domain.model.dtos.ProblemDto;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.resources.rest.controller.model.StockDtoHelperPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testGetAllStocks_Success() {
        String url = "/stock-service/v1/api/stocks?page=0&size=10";
        ResponseEntity<StockDtoHelperPage> response = restTemplate.getForEntity(url, StockDtoHelperPage.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(((List<StockDtoHelperPage>) response.getBody().getContent()), hasItems(
                hasToString(containsString("Stock 1")),
                hasToString(containsString("Stock 2"))
        ));
        assertThat(((StockDtoHelperPage) response.getBody()).getNumber(), is(0));
        assertThat(((StockDtoHelperPage) response.getBody()).getSize(), is(10));
    }


    @Test
    public void testGetAllStocks_InvalidParameters() {
        String url = "/stock-service/v1/api/stocks?page=0&size=0";
        ResponseEntity<ProblemDto> response = restTemplate.getForEntity(url, ProblemDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(response.getBody().getType(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        assertEquals(response.getBody().getTitle(), StockUtility.ILLEGAL_ARGUMENT_ERROR_TITLE);

    }

    @Test
    public void testCreateStock_Success() {
        String url = "/stock-service/v1/api/stocks";
        StockDto stock = new StockDto();
        stock.setName("Test Stock");
        stock.setCurrentPrice(BigDecimal.valueOf(100.00));
        ResponseEntity<StockDto> response = restTemplate.postForEntity(url, stock, StockDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response.getBody().getName(), stock.getName());
        assertEquals(response.getBody().getCurrentPrice(), stock.getCurrentPrice());
    }

    @Test
    public void testGetSingleStock_Success() {
        long stockId = 1L;
        StringBuilder url = new StringBuilder("/stock-service/v1/api/stocks/");
        url.append(stockId);

        ResponseEntity<StockDto> response = restTemplate.getForEntity(url.toString(), StockDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().getName(), "Stock 1");
    }

    @Test
    public void testGetSingleStock_StockNotFound() {
        long stockId = 999; // Assuming this ID does not exist in the database.
        StringBuilder url = new StringBuilder("/stock-service/v1/api/stocks/");
        url.append(stockId);
        ResponseEntity<ProblemDto> response = restTemplate.getForEntity(url.toString(), ProblemDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody().getStatus(), HttpStatus.NOT_FOUND.value());
        assertEquals(response.getBody().getType(), HttpStatus.NOT_FOUND.getReasonPhrase());
        assertEquals(response.getBody().getTitle(), StockUtility.STOCK_NOT_FOUND_ERROR_TITLE);
    }


}
