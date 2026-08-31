package com.ayad.jaxrsdemo.controller;

import com.ayad.jaxrsdemo.common.security.Authority;
import com.ayad.jaxrsdemo.config.JwtConfig;
import com.ayad.jaxrsdemo.domain.dtos.TestRequest;
import com.payconiq.customer.testing.v2.model.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class JaxRsControllerITJunit4 {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtConfig jwtConfig;

    protected static final Integer CLIENT_VERSION = 42;

    public static final String CLIENT_ID_HEADER = "client-id";
    public static final String CLIENT_VERSION_HEADER = "Client-Version";
    public static final String SDK_VERSION_HEADER = "SDK-Version";
    public static final String BETA_TYPE_HEADER = "X-Beta-Type";
    public static final String JAILBREAK_HEADER = "series";
    public static final String HARDWARE_ID_HEADER = "x-hardwareid";
    public static final String HEADER_ATTEMPTS_LEFT = "Attempts-Left";
    public static final String CF_CONNECTING_IP = "cf-connecting-ip";
    public static final String CORRELATION_ID_HEADER = "x-correlation-id";

    public static final String SIGNATURE = "Signature";
    public static final String X_REQUEST_ID = "X-Request-ID";

    protected static final Integer SDK_VERSION = 5;


    @Test
    public void testPost() {

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


    @Test
    public void testTokenPost() {

        TestRequest testRequest = new TestRequest("mohammed", "ayad");

        String jwt = jwtConfig.generateJWT("CONSUMER", "12345", "INT", 300, Authority.CUSTOMER.name());

//        String jwt = jwtConfig.generateToken("CONSUMER","12345",Authority.CUSTOMER.name());

        RequestEntity<TestRequest> requestEntity = RequestEntity.method(HttpMethod.POST, URI.create(""))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwt)
                .body(testRequest);


        ResponseEntity<TestRequest> response = restTemplate.
                exchange("/testing/customers/token-post-test",
                        HttpMethod.POST,
                        requestEntity,
                        TestRequest.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }


    @Test
    public void shouldCreateSctUser() {
        BankAccount bankAccount = new BankAccount()
                .iban("NL52INGB0007910404")
                .source("ING_BE")
                .accountHolderName("accountHolderName");

        VerifiedUserInfo verifiedUserInfo = new VerifiedUserInfo()
                .firstName("fname")
                .lastName("lname")
                .verifyingParty(VerifiedUserInfo.VerifyingPartyEnum.BANK);

        CreateCustomerRequest createCustomerRequest = createCustomerRequest(bankAccount, verifiedUserInfo);

        RequestEntity<CreateCustomerRequest> requestEntity = postAuthorizationHeader(
                "122323232",
                null,
                Authority.CUSTOMER.name(),
                createCustomerRequest,
                null);


        ResponseEntity<CreateCustomerResponse> responseEntity = restTemplate.exchange(
                "/testing/customers/",
                requestEntity.getMethod(),
                requestEntity,
                CreateCustomerResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        CreateCustomerResponse customerResponse = responseEntity.getBody();
        assertThat(customerResponse).isNotNull();
        assertThat(customerResponse.getId()).isNotEmpty();
        assertThat(customerResponse.getToken()).isNotEmpty();

    }


    protected <T> RequestEntity<T> postAuthorizationHeader(String subject, String hardwareId, String authority, T body,
                                                           String clientId) {
        try {
            return createRequestEntity(HttpMethod.POST, subject, hardwareId, authority, clientId, body);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create request entity");

        }
    }

    protected <T> RequestEntity<T> createRequestEntity(HttpMethod method, String subject, String hardwareId,
                                                       String authority,
                                                       String clientId, T body) {
        return createRequestEntity(method, "CONSUMER", subject, hardwareId, authority, clientId,
                String.valueOf(CLIENT_VERSION), null, body);
    }


    protected <T> RequestEntity<T> createRequestEntity(HttpMethod method, String subjectType, String subject,
                                                       String hardwareId, String authority, String clientId, String clientVersion, String ipAddress, T body) {
        String jwt = generateJWT(subjectType, subject, hardwareId, authority);
        RequestEntity.BodyBuilder bodyBuilder = RequestEntity.method(method, URI.create(""))
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .header(CLIENT_ID_HEADER, clientId)
                .header(CLIENT_VERSION_HEADER, clientVersion)
                .header(SDK_VERSION_HEADER,
                        String.valueOf(SDK_VERSION))
                .header(JAILBREAK_HEADER, "3")
                .header(HARDWARE_ID_HEADER, hardwareId)
                .header(CF_CONNECTING_IP, ipAddress);

        return bodyBuilder.body(body);
    }

    protected String generateJWT(String subjectType, String subject, String hardwareId, String authority) {
        return jwtConfig.generateJWT(subjectType, subject, "INT", 60, authority);
    }


    private static CreateCustomerRequest createCustomerRequest(BankAccount bankAccount, VerifiedUserInfo verifiedUserInfo) {
        Address address = new Address()
                .no("no")
                .country("BEL")
                .city("Kyiv")
                .postalCode("0110")
                .street("Leidsestraat x");

        Device device = new Device()
                .hardwareId(UUID.randomUUID().toString())
                .clientId("db6445066a40b88fa5d859f04ab190f5")
                .osVersion("Android 8");


        return new CreateCustomerRequest()
                .bankAccount(bankAccount)
                .address(address)
                .device(device)
                .firstName("firstName")
                .lastName("lastName")
                .languageTag(Locale.UK.toLanguageTag())
                .email("email1@payconiq.com")
                .emailConfirmed(true)
                .phone("+31683775555")
                .verifiedUserInfo(verifiedUserInfo);
    }


}
