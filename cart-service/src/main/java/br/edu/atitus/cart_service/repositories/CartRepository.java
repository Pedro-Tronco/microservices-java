package br.edu.atitus.cart_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.atitus.cart_service.dtos.CartItemDTO;
import br.edu.atitus.cart_service.entities.CartEntity;
import br.edu.atitus.cart_service.entities.CartId;
import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, CartId>{
	Page<CartEntity> findByUserId(Long userId, Pageable page);

	List<CartItemDTO> findByUserIdAndIsSelectedTrue(Long userId);
	
	CartEntity findByUserIdAndProductId(Long userId, Long productId);
	
	@Transactional
	void deleteByUserIdAndProductId(Long userId, Long productId);
}
