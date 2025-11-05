package com.ayad.citysorter.domain.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProblemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;

    private String title;

    private int status;

    private String detail;

    private String instance;
}
