package com.ayad.stockservice.domain.service.impl;

import com.ayad.stockservice.common.exception.StockNotFoundException;
import com.ayad.stockservice.common.utils.StockUtility;
import com.ayad.stockservice.domain.mapper.StockMapper;
import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.entities.Stock;
import com.ayad.stockservice.domain.repository.ifc.StockRepository;
import com.ayad.stockservice.domain.service.ifc.StockService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    public StockServiceImpl(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    /**
     * Retrieves a paginated list of all stocks.
     *
     * @param page The page number (zero-based) of the results to retrieve.
     * @param size The number of stocks per page.
     * @return A {@link Page} containing the list of stocks for the specified page and size.
     * the page number shouldn't be less than zero.
     * the size shouldn't be less than one.
     */
    @Override
    public Page<StockDto> getAllStocks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocksPage = stockRepository.findAll(pageable);
        return stocksPage.map(stockMapper::stockToStockDto);

    }

    /**
     * Create a new stock record in the database.
     *
     * @param stock The stock object to be created. It should contain the following properties:
     *              - name (String): The name of the stock (required, maximum length: 50 characters).
     *              - current_price (Decimal): The current price of the stock (required, maximum precision: 10, scale: 2).
     * @return A StockDto representing the created stock record.
     */
    @Override
    public StockDto createStock(StockDto stock) {
        Stock stockEntity = stockMapper.stockDtoToStock(stock);
        return stockMapper.stockToStockDto(stockRepository.save(stockEntity));

    }

    /**
     * Retrieves a single stock record from the database by its ID.
     *
     * @param id The ID of the stock record to retrieve.
     * @return A StockDto representing the requested stock record.
     * @throws StockNotFoundException If the requested stock record cannot be found in the database.
     */
    @Override
    public StockDto getStockById(long id) throws StockNotFoundException {
        try {
            return stockMapper.stockToStockDto(getStockEntityById(id));
        } catch (EntityNotFoundException e) {
            throw new StockNotFoundException(HttpStatus.NOT_FOUND, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id));
        }
    }

    /**
     * Updates the price of a single stock record in the database by its ID.
     *
     * @param id       The ID of the stock record to update.
     * @param newPrice The new price of the stock record.
     * @return A StockDto representing the updated stock record.
     * @throws StockNotFoundException If the requested stock record cannot be found in the database.
     */
    @Override
    public StockDto updateStockPrice(long id, BigDecimal newPrice) throws StockNotFoundException {
        try {
            Stock stock = getStockEntityById(id);
            stock.setCurrentPrice(newPrice);
            return stockMapper.stockToStockDto(stockRepository.save(stock));
        } catch (EntityNotFoundException e) {
            throw new StockNotFoundException(HttpStatus.NOT_FOUND, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id));
        }
    }


    /**
     * Deletes a single stock record from the database by its ID.
     *
     * @param id The ID of the stock record to delete.
     * @throws StockNotFoundException If the requested stock record cannot be found in the database.
     */
    @Override
    public void deleteStockById(long id) throws StockNotFoundException {
        try {
            stockRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new StockNotFoundException(HttpStatus.NOT_FOUND, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id));
        }
    }


    /**
     * Retrieves a single stock record from the database by its ID.
     *
     * @param id The ID of the stock record to retrieve.
     * @return A reference to the requested stock record.
     * @throws EntityNotFoundException If the requested stock record cannot be found in the database.
     */
    private Stock getStockEntityById(long id) throws EntityNotFoundException {
        return stockRepository.getReferenceById(id);
    }
}
