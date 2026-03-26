package org.afflato.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.afflato")
public class ExchangeratesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeratesApplication.class, args);
	}

}
