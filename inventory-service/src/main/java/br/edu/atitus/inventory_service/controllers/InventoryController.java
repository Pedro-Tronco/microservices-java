package br.edu.atitus.inventory_service.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

import org.springframework.data.domain.Sort;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
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
import br.edu.atitus.inventory_service.repositories.QueryInventoryRepository;

@RestController
@RequestMapping("/ws/inventory")
public class InventoryController {
	
	@Value("${server.port}")
	private int serverPort;
	
	private final InventoryRepository repository;
	
	private final QueryInventoryRepository queryRepository;
	
	private final ProductClient productClient;
	
	public InventoryController(InventoryRepository repository, ProductClient productClient, QueryInventoryRepository queryRepository) {
		this.repository = repository;
		this.queryRepository = queryRepository; 
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
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			item.setLastAccess(currentTimestamp);
			repository.save(item);
			BeanUtils.copyProperties(productClient.getProductById(dto.productId()), item);
			item.setEnviroment("Inventory-service running on port: "+serverPort+" - " + item.getEnviroment());
			return item;
		}).toList();
		
		return ResponseEntity.status(HttpStatus.CREATED).body(items);
	}
	
	@GetMapping
	public ResponseEntity<Page<InventoryEntity>> getInventory (
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String genreTag,
			@RequestParam(required = false) String lang,
			@RequestParam(required = false) boolean onlyFavorites,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType,
			@PageableDefault(page = 0, size = 15) 
			@SortDefaults({
                @SortDefault(sort = "favorite", direction = Sort.Direction.DESC),
                @SortDefault(sort = "lastAccess", direction = Sort.Direction.DESC)
            }) Pageable pageable) {
		
		var queryHasProductData = false;
		String productSqlQuery = "SELECT * FROM tb_product WHERE 1 = 1";
		String inventorySqlQuery = "SELECT * FROM tb_inventory WHERE user_id = " + userId;
		
		if(search != null) {
			queryHasProductData = true;
			search = search.toLowerCase();
			productSqlQuery = productSqlQuery + " AND ( LOWER(unaccent(title)) LIKE('%'||unaccent('"+search+"')||'%')";
			productSqlQuery = productSqlQuery + " OR LOWER(unaccent(author)) LIKE('%'||unaccent('"+search+"')||'%')";
			productSqlQuery = productSqlQuery + " OR LOWER(unaccent(publisher)) LIKE('%'||unaccent('"+search+"')||'%') )";
		}
		
		if(genreTag != null) {
			queryHasProductData = true;
			var firstSearch = true; 
			productSqlQuery = productSqlQuery + " AND (";
			
			String[] genreArray = genreTag.split(","); 
			List<String> genreList = new ArrayList<>(Arrays.asList(genreArray));
			for(String genre : genreList){
				String formatedGenre = String.format("%02d", Integer.parseInt(genre));
				if (firstSearch)
					firstSearch = false;
				else
					productSqlQuery = productSqlQuery + " OR ";
				productSqlQuery = productSqlQuery + "genre_tags LIKE('%'||'"+formatedGenre+"'||'%')";
			}
			productSqlQuery = productSqlQuery + " )";
		}
		
		if(lang != null) {
			queryHasProductData = true;
			productSqlQuery = productSqlQuery + " AND language = '"+lang.toLowerCase()+"'";
		}
		
		if(queryHasProductData) {
			List<Long> productIdList = productClient.getProductIdFromQuery(productSqlQuery);
			if(productIdList.isEmpty())
				return ResponseEntity.ok(Page.empty(pageable));
			inventorySqlQuery = inventorySqlQuery + " AND product_id IN(";
			var firstSearch = true; 
			for(Long productId : productIdList) {
				if (firstSearch)
					firstSearch = false;
				else
					inventorySqlQuery = inventorySqlQuery + ", ";
				inventorySqlQuery = inventorySqlQuery + productId;
			}
			inventorySqlQuery = inventorySqlQuery + ")";
		}

		if(onlyFavorites)
			inventorySqlQuery = inventorySqlQuery + " AND is_favorite = TRUE";
		
		ArrayList<InventoryEntity> inventoryList = queryRepository.findByQuery(inventorySqlQuery)
				.stream().map(item -> {
			BeanUtils.copyProperties(productClient.getProductById(item.getProductId()), item);
			item.setEnviroment("Inventory-service running on port: "+serverPort+" - " + item.getEnviroment());
			return item;
		}).collect(Collectors.toCollection(ArrayList::new));
		inventoryList.sort(
				Comparator.comparing(InventoryEntity::isFavorite)
				.thenComparing(InventoryEntity::getLastAccess).reversed()
				);
		Page<InventoryEntity> inventoryPage = new PageImpl<InventoryEntity>(inventoryList, pageable, inventoryList.size());
		
		return ResponseEntity.ok(inventoryPage);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<InventoryEntity> getByProductId(
			@PathVariable Long productId,
			@RequestHeader("X-User-Id") Long userId, 
			@RequestHeader("X-User-Email") String userEmail, 
			@RequestHeader("X-User-Type") int userType) throws Exception {
		var item = repository.findByUserIdAndProductId(userId, productId);
		if(item == null)
			throw new Exception("Product not found for user");
		
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		item.setLastAccess(currentTimestamp);
		repository.save(item);
		BeanUtils.copyProperties(productClient.getProductById(item.getProductId()), item);
		return ResponseEntity.ok(item);
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
		BeanUtils.copyProperties(productClient.getProductById(item.getProductId()), item);
		item.setEnviroment("Inventory-service running on port: "+serverPort+" - " + item.getEnviroment());
		return ResponseEntity.ok(item);
		
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handler(Exception e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(403).body(message);
	}
	
}
