package br.edu.atitus.review_service.controllers;

import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/reviews")
public class ReviewController {

	@Value("${server.port}")
	private int serverPort;
	
	private final ReviewRepository repository;
	private final AuthClient authClient;
	
	public ReviewController(
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
		Page<ReviewEntity> reviews = repository.findByProductid(productId, pageable);
		for(ReviewEntity review : reviews) {
			review.setUsername(authClient.getUsernameByUserId(productId).username());
			totalGrade += review.getGrade();
			totalReviews ++;
		}
		ReviewResponseDTO response = new ReviewResponseDTO();
		response.setReviews(reviews);
		response.setAvgGrade(totalGrade / totalReviews);
		response.setTotalReviews(totalReviews);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-user/{productId}")
	public ResponseEntity<ReviewEntity> getReviewByUserIdAndProductId(
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId
			) {
		var review = repository.findByProductIdAndUserId(productId, userId).get();
		review.setUsername(authClient.getUsernameByUserId(productId).username());
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
		review.setUsername(authClient.getUsernameByUserId(productId).username());
		return ResponseEntity.status(HttpStatus.CREATED).body(review);
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<ReviewEntity> updateReview
	
}
