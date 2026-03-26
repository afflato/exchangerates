package org.afflato.domain;

import lombok.RequiredArgsConstructor;
import org.afflato.domain.client.ExchangeRateClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateClient exchangeRateClient;

    public ExchangeRate getExchangeRate(String baseCurrency) {
        return exchangeRateClient.getLatestRate(baseCurrency);
    }
}
