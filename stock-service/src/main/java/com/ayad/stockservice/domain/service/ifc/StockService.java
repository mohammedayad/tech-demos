package com.ayad.stockservice.domain.service.ifc;

import com.ayad.stockservice.domain.model.entities.Stock;
import org.springframework.data.domain.Page;

public interface StockService {

    /**
     * Retrieves a paginated list of all stocks.
     *
     * @param page The page number (zero-based) of the results to retrieve.
     * @param size The number of stocks per page.
     * @return A {@link Page} containing the list of stocks for the specified page and size.
     * @throws IllegalArgumentException If the page number is less than zero.
     * @throws IllegalArgumentException If the size is less than one.
     */
    Page<Stock> getAllStocks(int page, int size);


    /**
     * Creates a new stock record in the database.
     *
     * @param stock The stock object to be created. It should contain the following properties:
     *              - name (String): The name of the stock (required, maximum length: 50 characters).
     *              - current_price (Decimal): The current price of the stock (required, maximum precision: 10, scale: 2).
     * @return The created stock record. The stock object will include the generated ID if the creation is successful.
     */
    Stock createStock(Stock stock);

    Stock getStockById(long id);
}
