package com.ayad.apigateway;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.config.DecoderConfig.ContentDecoder.DEFLATE;
import static io.restassured.config.DecoderConfig.decoderConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;

@AutoConfigureObservability
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ApiGatewayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApiGatewayApplicationTests {

    @LocalServerPort
    protected int port;
    @Value("${wiremock.server.port}")
    private int wireMockPort;

    @Test
    void checkObservabilityHeaders() {
        // Setup the mock endpoint.

        String path = "/integration-service/v1/.+/test";
        givenThat(post(urlPathMatching(path))
                .withHeader("X-B3-TraceId", matching(".*")) // b3 format
                .withHeader("X-B3-SpanId", matching(".*")) // b3 format
                .withHeader("traceparent", matching(".*")) // w3c format
                .willReturn(aResponse().withStatus(200).withBody("")));

        String requestPath = "/integration-service/v1/api/test";
        String address = gatewayAddress(port, requestPath);
        given().config(newConfig().decoderConfig(decoderConfig().contentDecoders(DEFLATE)))
//                .filter(filter)
                .body("{}")
                .contentType(ContentType.JSON)
                .when()
                .post(address)
                .prettyPeek()
                .then().statusCode(HttpStatus.OK.value());
    }

    private String gatewayAddress(int port, String path) {
        return String.format("http://localhost:%d%s", port, path);
    }

}
