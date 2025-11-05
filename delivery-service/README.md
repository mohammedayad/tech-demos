# delivery-service

## Details

 delivery-service is a microservice using Spring boot. The expected REST endpoints schema is described below.

### Endpoints

<table>
<tr>
   <td>Endpoint</td><td>Description</td><td>Request body example</td><td>Response body example</td>
</tr>
<!-- POST /deliveries -->
<tr>
   <td>POST /deliveries</td>
   <td>

   Creates a new delivery. <br>`status` is only allowed to be `IN_PROGRESS` or `DELIVERED`. For status `IN_PROGRESS` the `finishedAt` field must be `null`. For status `DELIVERED` the `finishedAt` field must be provided.
 
   </td>
   <td>

   ```json
   {
      "vehicleId": "AHV-589",
      "address": "Example street 15A",
      "startedAt": "2023-10-09T12:45:34.678Z",
      "status": "IN_PROGRESS"
   }
   ```

   </td>
   <td>

   ```json
   {
      "id": "69201507-0ae4-4c56-ac2d-75fbe27efad8",
      "vehicleId": "AHV-589",
      "address": "Example street 15A",
      "startedAt": "2023-10-09T12:45:34.678Z",
      "finishedAt": null,
      "status": "IN_PROGRESS" 
   }
   ```

   </td>
</tr>

<!-- POST /deliveries/invoice -->
<tr>
   <td>POST /deliveries/invoice</td>
   <td>
   
   Uses third party service (as defined in the [mock api](#mock-api)) to send invoices to customers.
   
   </td>
   <td>

   ```json
   {
      "deliveryIds": [
         "7167fc04-0625-49fc-98a9-8785a4a32b60"
      ]
   }
   ```

   </td>
   <td>

   ```json
   [
      { 
         "deliveryId": "7167fc04-0625-49fc-98a9-8785a4a32b60",
         "invoiceId": "e891827f-487f-4884-a8c3-77316212b81b"
      }
   ]
   ```

   </td>
</tr>

<!-- GET /deliveries/business-summary -->
<tr>
   <td>GET /deliveries/business-summary</td>
   <td colspan="2">
   
   Business wants a summary of yesterday's deliveries (Amsterdam time).<br>The summary must include how many deliveries were **started**. The summary should also include the average time between delivery start. This means if there are 3 deliveries that started at `01:00`, `03:00` and `09:00` the time between starting deliveries is `2 hours` (01:00-03:00) and `6 hours` (03:00 - 09:00) so the average is `4 hours` or `240 minutes`
   
   </td>
   <td>

   ```json
   {
      "deliveries": 3,
      "averageMinutesBetweenDeliveryStart": 240
   }
   ```

   </td>
</tr>
</table>

## Mock API
A mock API is exposed on port `8000` which is defined in the [docker-compose file](./docker-compose.yml#L4), this mock API must not be modified. The endpoint exposed in this API is used for the `/deliveries/invoice` task. The mock API exposes the following endpoint.
<!-- POST /v1/invoices -->
<table>
<tr>
   <td>Endpoint</td><td>Request body example</td><td>Response body example</td>
</tr>
<tr>
   <td>POST /v1/invoices</td>
   <td>

   ```json
   {
      "deliveryId": "7167fc04-0625-49fc-98a9-8785a4a32b60",
      "address": "Example street 15A"
   }
   ```

   </td>
   <td>

   ```json
   {
      "id": "e891827f-487f-4884-a8c3-77316212b81b",
      "sent": true
   }
   ```

   </td>
</tr>
</table>

## Where to start
- [A docker-compose file](./docker-compose.yml) already exists that builds and runs the application. Run this to make the [mock API](#mock-api) and database available. You can use the following command `docker-compose up --build`


## Postman Collection

A Postman collection is provided to help you test the API endpoints. You can find the collection file in the `postman/` directory.

### Importing the Collection

1. Open Postman.
2. Click on **Import** in the top-left corner.
3. Select the file `postman/delivery-service.postman_collection.json` from the project directory.
4. The collection will be imported, and you can use it to test the API endpoints.

### Collection Details

The collection includes the following requests:
- **POST /deliveries**: Create a new delivery.
- **POST /deliveries/invoice**: Send invoices for deliveries.
- **GET /deliveries/business-summary**: Retrieve a summary of deliveries.

Make sure the application is running on `http://localhost:8080` before using the collection.

## To-do and considerations
- Integration Tests.
- Pagination.
- Harden invoice client with retries, timeouts, circuit breaker.
- **Security**: Add authentication and authorization mechanisms (OAuth2/JWT or mTLS).
- Implement rate limiting to prevent abuse.
- **Observability**: Actuator enabled; structured JSON logs via Log4j2 (configure if needed).

## Built With
- Java
- Spring Boot
- OpenAPI 3 (Code First) [Deliveries UI](http://localhost:8080/swagger-delivery-service-ui.html)
- maven
- Liquibase
- H2 Database
- Junit
- Mockito
- Docker/Docker Compose
