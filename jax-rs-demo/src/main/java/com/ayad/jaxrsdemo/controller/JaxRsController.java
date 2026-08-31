package com.ayad.jaxrsdemo.controller;


import com.ayad.jaxrsdemo.common.security.Authority;
import com.ayad.jaxrsdemo.config.JwtConfig;
import com.ayad.jaxrsdemo.domain.dtos.TestRequest;
import com.ayad.jaxrsdemo.domain.dtos.TestResponse;
import com.payconiq.customer.testing.v2.model.CreateCustomerRequest;
import com.payconiq.customer.testing.v2.model.CreateCustomerResponse;
import com.payconiq.customer.v1.topic.CustomerUpdatesV1;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static com.ayad.jaxrsdemo.common.utils.ResourceAuthorizationExpressions.CONSUMER_WITH_CUSTOMER_AND_PROSPECT_AUTHORITY;
import static com.ayad.jaxrsdemo.common.utils.ResourceAuthorizationExpressions.CONSUMER_WITH_CUSTOMER_AUTHORITY;

@Component
@Path("/testing/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JaxRsController {

    @Autowired
    private JwtConfig jwtConfig;

    private static final Logger log = LoggerFactory.getLogger(JaxRsController.class);


    @GET
    @Path("/get-test")
    public Response testGet() {
        log.info("testGet()");
        return Response.ok(new TestResponse("success")).build();
    }


    @POST
    @Path("/post-test")
    public Response testPost(TestRequest testRequest) {
        log.info("testPost{}", testRequest);
        return Response.ok(testRequest).build();

    }


    @POST
    @Path("/token-post-test")
    @PreAuthorize(CONSUMER_WITH_CUSTOMER_AND_PROSPECT_AUTHORITY)
    public Response testTokenPost(TestRequest testRequest) {
        log.info("testPost{}", testRequest);
        return Response.ok(testRequest).build();

    }


    @POST
    @PreAuthorize(CONSUMER_WITH_CUSTOMER_AND_PROSPECT_AUTHORITY)
    public Response createCustomer(@Valid CreateCustomerRequest request) {
        String customerId = UUID.randomUUID().toString();
        String token = jwtConfig.generateJWT("CONSUMER", "12345", "INT", 300, Authority.CUSTOMER.name());

        // Hack to call keys/preregistration from tests as it looks up for authentication with status `CREATE`

        return Response.ok(new CreateCustomerResponse().id(customerId).token(token))
                .build();
    }
}
