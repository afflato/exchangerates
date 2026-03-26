package org.afflato.port;

import org.afflato.domain.ExchangeRate;
import org.afflato.domain.client.ExchangeRateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExchangeRateClientMockImpl implements ExchangeRateClient {
    private final RestClient restClient;

    public ExchangeRateClientMockImpl(RestClient.Builder restClientBuilder,
                      @Value("${service.exchangerates-mock.url}") String url) {
        this.restClient = restClientBuilder
                .baseUrl(url)
                .build();
    }

    @Override
    public ExchangeRate getLatestRate(String currency) {
        return restClient.get()
                .uri("")
                .retrieve().body(ExchangeRate.class);
    }
}
