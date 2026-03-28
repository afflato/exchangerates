package org.afflato.rest;

import lombok.RequiredArgsConstructor;
import org.afflato.domain.ExchangeRate;
import org.afflato.domain.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {
    public static final String USD = "USD";
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/latest/{currency}")
    public ResponseEntity<ExchangeRate> getData(@PathVariable String currency) {
        currency = USD.equals(currency) ? currency : USD;
        return ResponseEntity.ok(exchangeRateService.getExchangeRate(currency));
    }

    @SuppressWarnings("all")
    @GetMapping("/error/validation")
    public ResponseEntity<String> validation() {
        return new ResponseEntity<>("Validation failed", HttpStatus.BAD_REQUEST);
    }

    @SuppressWarnings("all")
    @GetMapping("/error/server")
    public ResponseEntity<String> unhandled(@PathVariable String currency) {
        return new ResponseEntity<>("Unknown Exception Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
