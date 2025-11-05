package com.ahold.technl.sandbox.domain.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


/**
 * Standardized error response format.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("type")
    private String type = "about:blank";

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private int status;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("instance")
    private String instance;
}
