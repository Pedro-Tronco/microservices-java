package br.edu.atitus.currency_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {
	
	@GetMapping("/auth/prefered-currency/{userId}")
	AuthResponse getPreferedCurrenctById(@PathVariable Long userId);
}
