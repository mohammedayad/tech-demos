package com.ayad.stockservice.domain.mapper;

import com.ayad.stockservice.domain.model.dtos.StockDto;
import com.ayad.stockservice.domain.model.entities.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {
    StockDto stockToStockDto(Stock stock);

    Stock stockDtoToStock(StockDto stockDto);
}
