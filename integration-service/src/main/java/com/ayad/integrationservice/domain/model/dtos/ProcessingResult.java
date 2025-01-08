package com.ayad.integrationservice.domain.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingResult {

    private String processId;
    private String status;
    private String result;
}
