package br.edu.atitus.inventory_service.clients;

public record ProductResponse(
	    Long id,
	    String title,
	    String author,
	    String synopsis,
	    String language,
		String publisher,
		String fileExtension,
		String genreTags,
		int pageCount,
		String downloadUrl,
	    String imageUrl
	) {}
