# Instructions

### Dependencies
- Maven
- Java 17

### Build

- ```./mvnw clean package ```

### Run

- ```java -jar ./target/tiny-ledger-1.0.0.jar ```

### Docs

1. Open a web browser and navigate to `http://localhost:8081/swagger-ui.html` to check the available rest endpoint and schema
2. Obtain the api-docs from http://localhost:8081/v3/api-docs and import it into your favourite API client (e.g. Postman)

### Tests

There is a cool integration test that checks the correctness of the ledger <br>
It is located in `src/test/java/com/tiny_ledger/TinyLedgerIT`

### Usage

1. Create a customer ```POST /tiny-ledger/customers``` and use the returned customer id for the next calls
2. Create as many transactions as you want for the customer using ```POST /tiny-ledger/transactions``` 
3. Get the balance of the customer using ```GET /tiny-ledger/customers/{customerId}/balance```
4. Get the transactions of the customer using ```GET /tiny-ledger/customers/{customerId}/transactions```

