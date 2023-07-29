package com.ayad.stockservice.domain.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class StockPriceDto {
    @NotNull(message = "New Price cannot be blank")
    @DecimalMin("0.01")
    private BigDecimal currentPrice;
}
