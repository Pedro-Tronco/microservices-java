package br.edu.atitus.currency_service.controllers;

import java.util.NoSuchElementException;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.currency_service.clients.AuthClient;
import br.edu.atitus.currency_service.clients.AuthResponse;
import jakarta.ws.rs.NotFoundException;

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
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handler(NotFoundException e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(404).body(message);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handlerAuth(IllegalArgumentException e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(400).body(message);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> handlerAuth(AuthenticationException e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(403).body(message);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handlerAuth(NoSuchElementException e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(404).body(message);
	}
}
