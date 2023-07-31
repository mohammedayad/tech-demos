package com.ayad.stockservice.domain.service.ifc;

import com.ayad.stockservice.common.exception.StockNotFoundException;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.entities.Stock;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface StockService {

    Page<StockDto> getAllStocks(int page, int size);


    /**
     * Creates a new stock record in the database.
     *
     * @param stock The stock object to be created. It should contain the following properties:
     *              - name (String): The name of the stock (required, maximum length: 50 characters).
     *              - current_price (Decimal): The current price of the stock (required, maximum precision: 10, scale: 2).
     * @return The created stock record. The stock object will include the generated ID if the creation is successful.
     */
    StockDto createStock(StockDto stock);

    StockDto getStockById(long id) throws StockNotFoundException;

    StockDto updateStockPrice(long id, BigDecimal newPrice) throws StockNotFoundException;

    void deleteStockById(long id) throws StockNotFoundException;


}
