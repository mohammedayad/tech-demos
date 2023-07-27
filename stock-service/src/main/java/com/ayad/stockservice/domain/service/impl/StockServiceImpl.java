package com.ayad.stockservice.domain.service.impl;

import com.ayad.stockservice.common.exception.StockNotFoundException;
import com.ayad.stockservice.common.utils.StockUtility;
import com.ayad.stockservice.domain.model.entities.Stock;
import com.ayad.stockservice.domain.repository.ifc.StockRepository;
import com.ayad.stockservice.domain.service.ifc.StockService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Page<Stock> getAllStocks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return stockRepository.findAll(pageable);

    }

    @Override
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock getStockById(long id) {
        try {
            Stock stock = stockRepository.getReferenceById(id);
            return stock;
        } catch (EntityNotFoundException e) {
            throw new StockNotFoundException(HttpStatus.BAD_REQUEST, StockUtility.CONSTRAINT_VIOLATIONS,
                    String.format(StockUtility.NO_Stock_MATCHING_FOUND, id));
        }

    }
}
