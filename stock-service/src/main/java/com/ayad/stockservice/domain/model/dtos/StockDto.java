package com.ayad.stockservice.domain.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class StockDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank(message = "stock name cannot be blank")
    private String name;
    @NotNull(message = "current Price cannot be blank")
    @DecimalMin("0.01")
    private BigDecimal currentPrice;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant lastUpdate;
}
