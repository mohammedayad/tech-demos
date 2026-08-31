package com.ayad.jaxrsdemo.controller;


import com.ayad.jaxrsdemo.domain.dtos.TestRequest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JaxRsControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void testPost() {

        TestRequest testRequest = new TestRequest("mohammed", "ayad");

        RequestEntity<TestRequest> requestEntity = RequestEntity.method(HttpMethod.POST, URI.create(""))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(testRequest);


        ResponseEntity<TestRequest> response = restTemplate.
                exchange("/testing/customers/post-test",
                        HttpMethod.POST,
                        requestEntity,
                        TestRequest.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
