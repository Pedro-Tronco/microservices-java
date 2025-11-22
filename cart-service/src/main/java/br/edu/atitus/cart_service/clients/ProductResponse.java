package br.edu.atitus.cart_service.clients;

import java.util.List;

public record ProductResponse(
	    Long id,
	    String title,
		String author,
		String synopsis,
		String language,
		String publisher,
		String fileExtension,
		int pageCount,
		String downloadUrl,
		String imageUrl,
		String enviroment,
		String genreTagsString,
		List<GenreTagResponse> genreTagsList,
		double price,
		String currency,
		double convertedPrice
	) {}
