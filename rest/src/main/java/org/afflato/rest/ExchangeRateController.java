package org.afflato.rest;

import lombok.RequiredArgsConstructor;
import org.afflato.domain.ExchangeRate;
import org.afflato.domain.ExchangeRateService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {
    public static final String USD = "USD";
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/latest/{currency}")
    public ExchangeRate getData(@PathVariable String currency) {
        currency = USD.equals(currency) ? currency : USD;
        return exchangeRateService.getExchangeRate(currency);
    }

}
