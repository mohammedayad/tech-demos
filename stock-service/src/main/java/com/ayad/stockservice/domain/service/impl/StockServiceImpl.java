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

    @Override
    public Page<StockDto> getAllStocks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Stock> stocksPage = stockRepository.findAll(pageable);
        return stocksPage.map(stockMapper::stockToStockDto);

    }

    @Override
    public StockDto createStock(StockDto stock) {
        Stock stockEntity = stockMapper.stockDtoToStock(stock);
        return stockMapper.stockToStockDto(stockRepository.save(stockEntity));

    }

    @Override
    public StockDto getStockById(long id) throws StockNotFoundException {
        try {
            return stockMapper.stockToStockDto(getStockEntityById(id));
        } catch (EntityNotFoundException e) {
            throw new StockNotFoundException(HttpStatus.NOT_FOUND, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id));
        }
    }

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

    @Override
    public void deleteStockById(long id) throws StockNotFoundException {
        try {
            stockRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new StockNotFoundException(HttpStatus.NOT_FOUND, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id));
        }


    }

    private Stock getStockEntityById(long id) throws EntityNotFoundException {
        return stockRepository.getReferenceById(id);
    }
}
