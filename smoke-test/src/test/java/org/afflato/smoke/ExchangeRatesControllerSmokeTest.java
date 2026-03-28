package org.afflato.smoke;

import org.afflato.app.ExchangeratesApplication;
import org.afflato.domain.ExchangeRate;
import org.afflato.domain.client.ExchangeRateClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ExchangeratesApplication.class
)
@ActiveProfiles("test")
public class ExchangeRatesControllerSmokeTest {
    public static final String DEFAULT_JSON_RESPONSE = """
            {
              "base": "USD",
              "rates": {
                "AED": 3.6725,
                "JPY": 153.232404,
                "USD": 1,
                "INR": 84.109224
              },
              "last_updated": "2025-03-09T14:37:43.515+00:00"
            }
            """;
    @LocalServerPort
    private int port;
    private RestClient restClient;


    @MockitoBean
    private ExchangeRateClient exchangeRateClient;

    @BeforeEach
    void setUp() {
        // Manually create the client pointing to your local running test server
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldReturnStatusCode200OkResponseForApplicationHealthApi() {
        // Act: Using the fluent API
        ResponseEntity<String> response = restClient.get()
                .uri("/actuator/health")
                .retrieve()
                .toEntity(String.class);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("\"status\":\"UP\""));
    }

    @Test
    void shouldReturnInvalid400StatusCode() {
        // Act: Using the fluent API
        ResponseEntity<String> response = restClient.get()
                .uri("/error/validation")
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> { })
                .toEntity(String.class);

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody());
    }

    @Test
    void shouldReturnUnknow500StatusCode() {
        // Act: Using the fluent API
        ResponseEntity<String> response = restClient.get()
                .uri("/error/server")
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> { })
                .toEntity(String.class);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void shouldReturnLatestExchangeRateForCurrency() {

        //Arrange
        String baseCurrency = "USD";
        when(exchangeRateClient.getLatestRate(any())).thenReturn(getExchangeRate());

        // Act: Convert RestTemplate.getForEntity to RestClient
        ResponseEntity<ExchangeRate> response = //restClientBuilder.build()
                restClient
                .get()
                .uri("/latest/{currency}", baseCurrency)
                .retrieve()
                .toEntity(ExchangeRate.class);

        //Assert
        ExchangeRate exchangeRate = response.getBody();
        assertThat(exchangeRate).isNotNull();
        assertThat(exchangeRate.getBaseCurrency()).isEqualTo(baseCurrency);
        assertThat(exchangeRate.getRates()).isNotEmpty();
    }

    public ExchangeRate getExchangeRate() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("AED", 3.6725);
        rates.put("JPY", 153.232404);
        rates.put("INR", 84.109224);

        return new ExchangeRate("USD", rates);
    }

    private String jsonResponse() {
        return DEFAULT_JSON_RESPONSE;
    }
}