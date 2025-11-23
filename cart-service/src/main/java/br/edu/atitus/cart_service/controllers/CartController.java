package br.edu.atitus.cart_service.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

import br.edu.atitus.cart_service.clients.ProductClient;
import br.edu.atitus.cart_service.dtos.CartDTO;
import br.edu.atitus.cart_service.dtos.CartItemDTO;
import br.edu.atitus.cart_service.dtos.CartResponseDTO;
import br.edu.atitus.cart_service.entities.CartEntity;
import br.edu.atitus.cart_service.repositories.CartRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/ws/cart")
public class CartController {

	@Value("${server.port}")
	private int serverPort;
	private final CartRepository repository;
	private final ProductClient productClient;
	private final CacheManager cacheManager;

	
	public CartController(CartRepository repository, ProductClient productClient, CacheManager cacheManager) {
		this.repository = repository;
		this.productClient = productClient;
		this.cacheManager = cacheManager;
	}
	
	@GetMapping("/{targetCurrency}")
	public ResponseEntity<CartResponseDTO> getCart(
			@PathVariable String targetCurrency,
			@RequestHeader("X-User-Id") Long userId,
			@PageableDefault(page = 0, size = 15, sort = "inclusion") 
	 Pageable pageable) {
		double totalPrice = 0;
		Page<CartEntity> cart = repository.findByUserId(userId, pageable);
		for(CartEntity item : cart) {
			String nameCache = "CartCache";
			String keyCache = item.getProductId() + item.getCurrency();
			var product = cacheManager.getCache(nameCache).get(keyCache, CartEntity.class);
			if(product != null)
				BeanUtils.copyProperties(product, item);
			else {
				BeanUtils.copyProperties(productClient.getProductByIdWithCurrency(item.getProductId(), targetCurrency), item);
				cacheManager.getCache(nameCache).put(keyCache, item);
			}
			if (item.isSelected())
				totalPrice += item.getConvertedPrice();
			item.setEnviroment("Cart-service running on port: "+serverPort+" - " + item.getEnviroment());
		}
		CartResponseDTO cartResponse = new CartResponseDTO();
		cartResponse.setItems(cart);
		cartResponse.setTotalValue(totalPrice);
		return ResponseEntity.ok(cartResponse);
	}
	
	@PostMapping
	public ResponseEntity<Page<CartEntity>> addToCart(
			@RequestBody CartDTO cartDTO,
			@RequestHeader("X-User-Id") Long userId,
			@PageableDefault(page = 0, size = 15, sort = "inclusion") 
			 Pageable pageable) {
		ArrayList<CartEntity> items = cartDTO.items().stream().map(dto -> {
			CartEntity item = new CartEntity();
			item.setProductId(dto.productId());
			item.setUserId(userId);
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			item.setInclusion(currentTimestamp);
			item.setSelected(true);
			repository.save(item);
			BeanUtils.copyProperties(
					productClient.getProductById(
							dto.productId()
					), item);
			item.setEnviroment("Cart-service running on port: "+serverPort+" - "+item.getEnviroment());
			return item;
		}).collect(Collectors.toCollection(ArrayList::new));
		items.sort(Comparator.comparing(CartEntity::getInclusion).reversed());
		Page<CartEntity> cart = new PageImpl<CartEntity>(items, pageable, items.size());
		return ResponseEntity.status(HttpStatus.CREATED).body(cart);
	}
	
	@DeleteMapping
	public ResponseEntity<List<CartItemDTO>> processPurchase(@RequestHeader("X-User-Id") Long userId) {
		List<CartItemDTO> items = repository.findByUserIdAndIsSelectedTrue(userId).stream().map(dto -> {
			repository.deleteByUserIdAndProductId(userId, dto.productId());
			return dto;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(items);
	}
	
	@PutMapping("/select/{productId}/{currency}")
	public ResponseEntity<CartEntity> setSelected(
			@PathVariable Long productId,
			@PathVariable String currency,
			@RequestHeader("X-User-Id") Long userId
			) throws Exception {
		var item = repository.findByUserIdAndProductId(userId, productId).get();
		if (item == null)
			throw new NotFoundException("Product not found in cart");
		
		if(item.isSelected())
			item.setSelected(false);
		else
			item.setSelected(true);
		
		repository.save(item);
		String nameCache = "CartCache";
		String keyCache = item.getProductId() + item.getCurrency();
		BeanUtils.copyProperties(productClient.getProductByIdWithCurrency(item.getProductId(), currency), item);
		cacheManager.getCache(nameCache).put(keyCache, item);
		item.setEnviroment("Cart-service running on port: "+serverPort+" - " + item.getEnviroment());
		
		return ResponseEntity.ok(item);
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
