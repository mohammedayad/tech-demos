package com.ayad.usertracking.domain.model;


import com.ayad.usertracking.domain.enums.Color;
import com.ayad.usertracking.domain.enums.DesignType;
import com.ayad.usertracking.domain.enums.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private Color color;

    private ProductType type;

    private DesignType designType;

}
