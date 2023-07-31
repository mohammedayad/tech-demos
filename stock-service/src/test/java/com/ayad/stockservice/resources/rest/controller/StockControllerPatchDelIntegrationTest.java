package com.ayad.stockservice.resources.rest.controller;

import com.ayad.stockservice.domain.model.dtos.ProblemDto;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.dtos.StockPriceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockControllerPatchDelIntegrationTest {

    private RestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate.setRequestFactory(requestFactory);

    }


    @Test
    public void testUpdateStockPrice_Success() {
        long stockId = 3L;
        StockPriceDto newPrice = new StockPriceDto();
        BigDecimal price = BigDecimal.valueOf(150.00);
        newPrice.setCurrentPrice(price);
       // String url = "http://localhost:" + port + "/stock-service/v1/api/stocks/{id}";
        StringBuilder url = new StringBuilder("http://localhost:");
        url.append(port);
        url.append("/stock-service/v1/api/stocks/{id}");
        // Test success case
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StockPriceDto> requestEntity = new HttpEntity<>(newPrice, headers);

        ResponseEntity<StockDto> responseEntity = restTemplate.exchange(
                url.toString(),
                HttpMethod.PATCH,
                requestEntity,
                StockDto.class,
                stockId
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody().getName(), "Stock 3");
        assertEquals(responseEntity.getBody().getCurrentPrice(), price);
    }

    @Test
    public void testUpdateStockPrice_StockNotFound() {
        long stockId = 999L;
        try {

            StockPriceDto newPrice = new StockPriceDto();
            BigDecimal price = BigDecimal.valueOf(150.00);
            newPrice.setCurrentPrice(price);
           // String url = "http://localhost:" + port + "/stock-service/v1/api/stocks/{id}";
            StringBuilder url = new StringBuilder("http://localhost:");
            url.append(port);
            url.append("/stock-service/v1/api/stocks/{id}");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<StockPriceDto> requestEntity = new HttpEntity<>(newPrice, headers);
            restTemplate.exchange(
                    url.toString(),
                    HttpMethod.PATCH,
                    requestEntity,
                    ProblemDto.class,
                    stockId
            );
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode(), is(HttpStatus.NOT_FOUND));
        }


    }



    @Test
    public void testDeleteStock_Success() throws Exception {
        // Create a new stock to use in the test
        StockDto stock = new StockDto();
        stock.setName("Test Stock");
        stock.setCurrentPrice(BigDecimal.valueOf(10.99));
        //String url = "http://localhost:" + port + "/stock-service/v1/api/stocks/";
        StringBuilder url = new StringBuilder("http://localhost:");
        url.append(port);
        url.append("/stock-service/v1/api/stocks/");

        String stockJson = new ObjectMapper().writeValueAsString(stock);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url.toString());
        httpPost.setEntity(new StringEntity(stockJson));
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        long createdStockId = new ObjectMapper().readTree(responseJson).get("id").asLong();
        // Perform DELETE request using the ID of the created stock
        HttpDelete httpDelete = new HttpDelete(url.append(createdStockId).toString());
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        // Verify response status code
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.NO_CONTENT.value()));
        // Close resources
        response.close();
        httpClient.close();
    }
}

