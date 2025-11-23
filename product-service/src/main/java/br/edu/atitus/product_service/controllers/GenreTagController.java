package br.edu.atitus.product_service.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.security.sasl.AuthenticationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.entities.GenreTagEntity;
import br.edu.atitus.product_service.repositories.GenreTagRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("products/tags")
public class GenreTagController {
	
	private final GenreTagRepository repository;
	
	public GenreTagController(GenreTagRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping
	public ResponseEntity<List<GenreTagEntity>> getAll() {
		List<GenreTagEntity> genreList = repository.findAll();
		return ResponseEntity.ok(genreList);
	}
	
	@GetMapping("/{genreTagsString}")
	public List<GenreTagEntity> getTagsListFromString(
			@PathVariable String genreTagsString) {
		String[] tagsStringArray = genreTagsString.split(",");
		Long[] tagsLongArray = Arrays.stream(tagsStringArray)
				.map(Long::parseLong)
                .toArray(Long[]::new);
		List<Long> tagsLongList = new ArrayList<>(Arrays.asList(tagsLongArray));
		List<GenreTagEntity> genreTagsList = repository.findAllById(tagsLongList);
		return genreTagsList;
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
