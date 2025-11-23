package br.edu.atitus.review_service.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.atitus.review_service.entities.ReviewEntity;
import br.edu.atitus.review_service.entities.ReviewId;

public interface ReviewRepository extends JpaRepository<ReviewEntity, ReviewId>{
	Page<ReviewEntity> findByProductid(Long productId, Pageable pageable);
	
	Optional<ReviewEntity> findByProductIdAndUserId(Long productId, Long userId);
}
