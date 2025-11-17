package br.edu.atitus.product_service.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.entities.GenreTagEntity;
import br.edu.atitus.product_service.repositories.GenreTagRepository;

@RestController
@RequestMapping("products/tags")
public class GenreTagController {
	
	private final GenreTagRepository tagRepository;
	
	public GenreTagController(GenreTagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}
	
	@Value("${server.port}")
	private int serverPort;
	
	@GetMapping("/from-string/{genreTagsString}")
	public List<GenreTagEntity> getTagsListFromString(
			@PathVariable String genreTagsString) {
		String[] tagsStringArray = genreTagsString.split(",");
		Long[] tagsLongArray = Arrays.stream(tagsStringArray)
				.map(Long::parseLong)
                .toArray(Long[]::new);
		List<Long> tagsLongList = new ArrayList<>(Arrays.asList(tagsLongArray));
		List<GenreTagEntity> genreTagsList = tagRepository.findAllById(tagsLongList);
		return genreTagsList;
	}
}
