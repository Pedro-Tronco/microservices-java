package br.edu.atitus.order_service.clients;

import java.util.List;

public record ProductResponse(
	    Long id,
	    String title,
	    String author,
	    String synopsis,
	    String language,
		String publisher,
		String fileExtension,
		String genreTagsString,
		List<GenreTagResponse> genreTagsList,
		int pageCount,
		String downloadUrl,
	    double price,
	    String currency,
	    String imageUrl,
	    String enviroment,
	    double convertedPrice
	) {}