package org.afflato.domain.client;

import org.afflato.domain.ExchangeRate;

public interface ExchangeRateClient {
    ExchangeRate getLatestRate(String currency);
}
