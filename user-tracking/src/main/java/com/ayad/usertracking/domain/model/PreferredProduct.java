package com.ayad.usertracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PreferredProduct {

    private String color;

    private String type;

    private String designType;

}
