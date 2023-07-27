package com.ayad.stockservice.domain.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class StockDto {
    private String name;
    private BigDecimal currentPrice;
    private Instant lastUpdate;
}
