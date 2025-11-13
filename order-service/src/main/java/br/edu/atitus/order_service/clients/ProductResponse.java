package br.edu.atitus.order_service.clients;

public record ProductResponse(
	    Long id,
	    String title,
	    String author,
	    String synopsis,
	    String language,
		String publisher,
		String file_extension,
		String genre_tags,
		int page_count,
		String download_url,
	    double price,
	    String currency,
	    String imageUrl,
	    String enviroment,
	    double convertedPrice
	) {}