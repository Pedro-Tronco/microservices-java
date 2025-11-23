package br.edu.atitus.review_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {
	
	@GetMapping("/auth/username/{userId}")
	AuthResponse getUsernameByUserId(@PathVariable Long userId);
}