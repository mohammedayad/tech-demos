package com.ayad.jaxrsdemo.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TestRequest(String firstName, String lastName) {
}
