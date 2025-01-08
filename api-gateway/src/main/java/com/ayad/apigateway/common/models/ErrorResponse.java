package com.ayad.apigateway.common.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String code;
    private String message;
    // An optional value detailing user blocked related error codes, possible value FRAUD, KYC
    private String blockDetail;

    // Enhanced fields, fetched from the token
    private String tokenType;
    private String subjectType;

    // Optional fields, not provided by all services
    private String requestId;
    private String traceId;
    private String spanId;

    // Migration to PQbyBC fields
    private String iosDownloadUrl;
    private String androidDownloadUrl;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

