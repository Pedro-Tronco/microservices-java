package br.edu.atitus.inventory_service.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.atitus.inventory_service.entities.InventoryEntity;
import br.edu.atitus.inventory_service.entities.InventoryId;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryId> {
	
	InventoryEntity findByUserIdAndProductId(Long userId, Long productId);
	
	Page<InventoryEntity> findByUserId(Long userId, Pageable pageable);

	
}
