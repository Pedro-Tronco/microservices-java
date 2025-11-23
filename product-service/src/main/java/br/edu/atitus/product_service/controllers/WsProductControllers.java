package br.edu.atitus.product_service.controllers;

import java.util.NoSuchElementException;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.dtos.ProductDTO;
import br.edu.atitus.product_service.entities.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/ws/products")
public class WsProductControllers {

	private final ProductRepository repository;

	public WsProductControllers(ProductRepository repository) {
		super();
		this.repository = repository;
	}
	
	private ProductEntity convertDto2Entity(ProductDTO dto) {
		var product = new ProductEntity();
		BeanUtils.copyProperties(dto, product);
		return product;
	}
	
	@PostMapping
	public ResponseEntity<ProductEntity> post(
			@RequestBody ProductDTO dto,
			@RequestHeader("X-User-Id") Long userId, 
			@RequestHeader("X-User-Email") String userEmail, 
			@RequestHeader("X-User-Type") Long userType ) throws Exception {
		
		if (userType != 0 && userType != 2)
			throw new AuthenticationException("Usuário sem permissão");
		
		var product = convertDto2Entity(dto);
		product.setUserId(userId);
		repository.save(product);
		
		return ResponseEntity.status(201).body(product);
	}
	
	@PutMapping("/{idProduct}")
	public ResponseEntity<ProductEntity> put(
			@PathVariable Long idProduct,
			@RequestBody ProductDTO dto,
			@RequestHeader("X-User-Id") Long userId, 
			@RequestHeader("X-User-Email") String userEmail, 
			@RequestHeader("X-User-Type") int userType ) throws Exception {
		
		if (userType != 0 && repository.findById(idProduct).get().getUserId() != userId)
			throw new AuthenticationException("Usuário sem permissão");
		
		
		var product = convertDto2Entity(dto);
		product.setId(idProduct);
		product.setUserId(userId);
		repository.save(product);
		
		return ResponseEntity.status(200).body(product);
	}
	
	@DeleteMapping("/{idProduct}")
	public ResponseEntity<String> delete(
			@PathVariable Long idProduct,
			@RequestHeader("X-User-Id") Long userId, 
			@RequestHeader("X-User-Email") String userEmail, 
			@RequestHeader("X-User-Type") Long userType ) throws Exception {
		
		if (userType != 0 && repository.findById(idProduct).get().getUserId() != userId)
			throw new AuthenticationException("Usuário sem permissão");
		
		repository.deleteById(idProduct);
		
		return ResponseEntity.ok("Produto " + idProduct + " excluído com sucesso");
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
