package br.edu.atitus.review_service.controllers;

import java.sql.Timestamp;
import java.util.NoSuchElementException;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import br.edu.atitus.review_service.dtos.ReviewResponseDTO;
import br.edu.atitus.review_service.entities.ReviewEntity;
import br.edu.atitus.review_service.repositories.ReviewRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/reviews")
public class OpenReviewController {

	@Value("${server.port}")
	private int serverPort;
	
	private final ReviewRepository repository;
	private final AuthClient authClient;
	
	public OpenReviewController(
			ReviewRepository repository,
			AuthClient authClient
			) {
		this.repository = repository;
		this.authClient = authClient;
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ReviewResponseDTO> getReviewsByProductId(
			@PathVariable Long productId,
			@PageableDefault(page = 0, size = 10, sort = "postDate", direction = Direction.DESC) 
			Pageable pageable) {
		double totalGrade = 0;
		int totalReviews = 0;
		Page<ReviewEntity> reviews = repository.findByProductId(productId, pageable);
		for(ReviewEntity review : reviews) {
			review.setUsername(authClient.getUsernameByUserId(review.getUserId()).name());
			totalGrade += review.getGrade();
			totalReviews ++;
		}
		ReviewResponseDTO response = new ReviewResponseDTO();
		response.setReviews(reviews);
		if(totalReviews > 0)
			response.setAvgGrade(totalGrade / totalReviews);
		else
			response.setAvgGrade(0);
		response.setTotalReviews(totalReviews);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<Page<ReviewEntity>> getAllReviews(
			@PageableDefault(page = 0, size = 30, sort = "postDate", direction = Direction.DESC) 
			Pageable pageable) {
		Page<ReviewEntity> reviews = repository.findAll(pageable);
		for(ReviewEntity review : reviews) {
			review.setUsername(authClient.getUsernameByUserId(review.getUserId()).name());
		}
		return ResponseEntity.ok(reviews);
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
