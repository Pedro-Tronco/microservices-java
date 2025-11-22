package br.edu.atitus.currency_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.currency_service.clients.AuthClient;
import br.edu.atitus.currency_service.clients.AuthResponse;

@RestController
@RequestMapping("ws/currency")
public class WsCurrencyController {

	private final AuthClient authClient;
	
	public WsCurrencyController(AuthClient authClient) {
		this.authClient = authClient;
	}
	
	@GetMapping("/prefered-currency")
	public ResponseEntity<AuthResponse> getPreferedCurrencyByUser(@RequestHeader("X-User-Id") Long userId) {
		return ResponseEntity.ok(authClient.getPreferedCurrenctById(userId));
	}
}
