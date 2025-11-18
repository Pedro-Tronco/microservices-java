package br.edu.atitus.inventory_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.atitus.inventory_service.entities.InventoryEntity;
import br.edu.atitus.inventory_service.entities.InventoryId;
import jakarta.transaction.Transactional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryId> {
	
	InventoryEntity findByUserIdAndProductId(Long userId, Long productId);
	
	Page<InventoryEntity> findByUserId(Long userId, Pageable pageable);
	
	@Transactional
	void deleteByUserIdAndProductId(Long userId, Long productId);
}
