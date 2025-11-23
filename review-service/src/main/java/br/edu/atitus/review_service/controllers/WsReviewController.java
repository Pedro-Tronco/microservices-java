package br.edu.atitus.review_service.controllers;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.review_service.clients.AuthClient;
import br.edu.atitus.review_service.dtos.ReviewDTO;
import br.edu.atitus.review_service.entities.ReviewEntity;
import br.edu.atitus.review_service.repositories.ReviewRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/ws/reviews")
public class WsReviewController {

	@Value("${server.port}")
	private int serverPort;
	
	private final ReviewRepository repository;
	private final AuthClient authClient;
	
	public WsReviewController(
			ReviewRepository repository,
			AuthClient authClient
			) {
		this.repository = repository;
		this.authClient = authClient;
	}
	
	@GetMapping("/by-user/{productId}")
	public ResponseEntity<ReviewEntity> getReviewByUserIdAndProductId(
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId
			) {
		var review = repository.findByProductIdAndUserId(productId, userId).get();
		review.setUsername(authClient.getUsernameByUserId(review.getUserId()).name());
		return ResponseEntity.ok(review);
	}
	
	@PostMapping("/{productId}")
	public ResponseEntity<ReviewEntity> postReview(
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestBody ReviewDTO dto
			) {
		ReviewEntity review = new ReviewEntity();
		review.setProductId(productId);
		review.setUserId(userId);
		BeanUtils.copyProperties(dto, review);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		review.setPostDate(currentTimestamp);
		repository.save(review);
		review.setUsername(authClient.getUsernameByUserId(review.getUserId()).name());
		return ResponseEntity.status(HttpStatus.CREATED).body(review);
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<ReviewEntity> updateReview(
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestBody ReviewDTO dto) {
		var review = repository.findByProductIdAndUserId(productId, userId).get();
		BeanUtils.copyProperties(dto, review);
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		review.setPostDate(currentTimestamp);
		repository.save(review);
		review.setUsername(authClient.getUsernameByUserId(review.getUserId()).name());
		return ResponseEntity.ok(review);
	}
	
	@DeleteMapping("/{productId}") 
	public ResponseEntity<String> deleteReview(
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId) {
		repository.deleteByProductIdAndUserId(productId, userId);
		return ResponseEntity.ok("Review deletada com sucesso");
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
