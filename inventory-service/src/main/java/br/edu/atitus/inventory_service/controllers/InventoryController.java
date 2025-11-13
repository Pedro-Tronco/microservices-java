package br.edu.atitus.inventory_service.controllers;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.inventory_service.clients.ProductClient;
import br.edu.atitus.inventory_service.clients.ProductResponse;
import br.edu.atitus.inventory_service.dtos.InventoryListDTO;
import br.edu.atitus.inventory_service.entities.InventoryEntity;
import br.edu.atitus.inventory_service.repositories.InventoryRepository;

@RestController
@RequestMapping("/ws/inventory")
public class InventoryController {
	
	private final InventoryRepository repository;
	
	private final ProductClient productClient;
	
	public InventoryController(InventoryRepository repository, ProductClient productClient) {
		this.repository = repository;
		this.productClient = productClient;
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
			
			ProductResponse response = productClient.getProductById(dto.productId());
			BeanUtils.copyProperties(response, item);			
			
			repository.save(item);
			return item;
		}).toList();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(items);
	}
	
	@GetMapping
	public ResponseEntity<Page<InventoryEntity>> getInventory (
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType,
			@PageableDefault(page = 0, size = 15, sort = "isFavorite", direction = Direction.DESC) 
			Pageable pageable) {
		Page<InventoryEntity> inventory = repository.findByUserId(userId, pageable);
		
		return ResponseEntity.ok(inventory);
	}
	
	@PutMapping("/favorite/{productId}") 
	public ResponseEntity<InventoryEntity> setFavorite (
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId, 
			@RequestHeader("X-User-Email") String userEmail, 
			@RequestHeader("X-User-Type") int userType ) throws Exception  {
		
		var item = repository.findByUserIdAndProductId(userId, productId);
		
		if(item == null)
			throw new Exception("Product not found for user");
		
		if(item.isFavorite() == false)
			item.setFavorite(true);
		else
			item.setFavorite(false);
		
		repository.save(item);
		
		return ResponseEntity.ok(item);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handler(Exception e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(403).body(message);
	}
			
}
