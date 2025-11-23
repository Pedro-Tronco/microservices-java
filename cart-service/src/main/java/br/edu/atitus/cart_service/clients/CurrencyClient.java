package br.edu.atitus.cart_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "currency-service")
public interface CurrencyClient {
	
	@GetMapping("/currency/prefered-currency")
	PreferedCurrencyResponse getPreferedCurrenctById(@RequestHeader("X-User-Id") Long userId);
}
