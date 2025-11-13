package br.edu.atitus.inventory_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.inventory_service.dtos.InventoryDTO;
import br.edu.atitus.inventory_service.dtos.InventoryListDTO;
import br.edu.atitus.inventory_service.entities.InventoryEntity;
import br.edu.atitus.inventory_service.repositories.InventoryRepository;

@RestController
@RequestMapping("/ws/inventory")
public class InventoryController {
	
	private final InventoryRepository repository;
	
	public InventoryController(InventoryRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<List<InventoryEntity>> addToInventory(
			@RequestBody InventoryListDTO inventoryDTO,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) {
		
		List<InventoryEntity> items = inventoryDTO.items().stream().map(dto -> {
			InventoryEntity item = new InventoryEntity();
			
			item.setProductId(dto.productId());
			item.setUserId(userId);
			
			repository.save(item);
			
			return item;
		}).toList();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(items);
	}
			
}
