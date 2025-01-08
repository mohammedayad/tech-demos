package com.ayad.integrationservice.domain.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProblemDto implements Serializable {
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
