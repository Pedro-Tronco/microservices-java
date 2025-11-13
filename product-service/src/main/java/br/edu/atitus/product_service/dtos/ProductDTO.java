package br.edu.atitus.product_service.dtos;

public record ProductDTO(
		String title,
		String author,
		String synopsis,
		String language,
		String publisher,
		String fileExtension,
		String genreTags,
		int pageCount,
		String currency,
		double price,
		String downloadUrl,
		String imageUrl
		) {

}
