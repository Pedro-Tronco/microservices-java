package br.edu.atitus.product_service.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.entities.GenreTagEntity;
import br.edu.atitus.product_service.repositories.GenreTagRepository;

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
}
