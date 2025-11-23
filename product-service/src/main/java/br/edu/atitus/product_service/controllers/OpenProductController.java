package br.edu.atitus.product_service.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.clients.CurrencyClient;
import br.edu.atitus.product_service.clients.CurrencyResponse;
import br.edu.atitus.product_service.entities.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;
import br.edu.atitus.product_service.repositories.QueryProductRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("products")
public class OpenProductController {

	private final ProductRepository repository;
	
	private final QueryProductRepository queryRepository;
	
	private final CurrencyClient currencyClient;
	
	private final GenreTagController tagsController;
	
	private final CacheManager cacheManager;

	public OpenProductController(
			ProductRepository repository, 
			CurrencyClient currencyClient, 
			CacheManager cacheManager, 
			QueryProductRepository queryRepository,
			GenreTagController tagsController
			) {
		super();
		this.repository = repository;
		this.currencyClient = currencyClient;
		this.cacheManager = cacheManager;
		this.queryRepository = queryRepository;
		this.tagsController = tagsController;
	}
	
	@Value("${server.port}")
	private int serverPort;
	
	@GetMapping("/{idProduct}/{targetCurrency}")
	public ResponseEntity<ProductEntity> getProduct(
			@PathVariable Long idProduct,
			@PathVariable String targetCurrency
			) throws Exception {
		
		targetCurrency.toUpperCase();
		
		String dataSource = "None";
		String keyCache = idProduct + targetCurrency;
		String nameCache = "ProductCache";
		
		ProductEntity product = cacheManager.getCache(nameCache).get(keyCache, ProductEntity.class);
		
		if(product != null) {
			dataSource = "Cache";
		} else {
			product = new ProductEntity();
			product = repository.findById(idProduct)
						.orElseThrow(() -> new Exception("Product not found"));
		
			if (targetCurrency.equalsIgnoreCase(product.getCurrency())) {
					product.setConvertedPrice(product.getPrice());
					dataSource = "None (target value equals registered value)";
			} else {
				CurrencyResponse currency = currencyClient.getCurrency(
						product.getPrice(),
						product.getCurrency(),
						targetCurrency);
				
				product.setConvertedPrice(currency.getConvertedValue());
				dataSource = "Currency Service (" + currency.getEnviroment() + ")";
				
			}
			product.setGenreTagsList(tagsController.getTagsListFromString(product.getGenreTagsString()));
		}
		
		if(product.getConvertedPrice() != -1)
				cacheManager.getCache(nameCache).put(keyCache, product);
		
		product.setEnviroment("Product Service running on port: " + serverPort + " | Source: " + dataSource);
		
		return ResponseEntity.ok(product);
	}
	
	@GetMapping("/noconverter/{idProduct}")
	public ResponseEntity<ProductEntity> getNoConverter(@PathVariable Long idProduct) throws Exception {
		var product = repository.findById(idProduct).orElseThrow(() -> new Exception("Produto não encontrado!"));
		product.setEnviroment("Product-service running on Port: " + serverPort);
		product.setGenreTagsList(tagsController.getTagsListFromString(product.getGenreTagsString()));
		return ResponseEntity.ok(product);
	}
	
	@GetMapping("/{targetCurrency}")
	public ResponseEntity<Page<ProductEntity>> getAllProducts(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String genreTag,
			@RequestParam(required = false) String lang,
			@PathVariable String targetCurrency,
			@PageableDefault(page = 0, size = 15, sort = "title", direction = Direction.ASC)
			Pageable pageable) throws Exception {
		
		String productSqlQuery = "SELECT * FROM tb_product WHERE 1 = 1";
		
		if(search != null) {
			search = search.toLowerCase();
			productSqlQuery = productSqlQuery + " AND ( LOWER(unaccent(title)) LIKE('%'||unaccent('"+search+"')||'%')";
			productSqlQuery = productSqlQuery + " OR LOWER(unaccent(author)) LIKE('%'||unaccent('"+search+"')||'%')";
			productSqlQuery = productSqlQuery + " OR LOWER(unaccent(publisher)) LIKE('%'||unaccent('"+search+"')||'%') )";
		}
		
		if(genreTag != null) {
			var firstSearch = true; 
			productSqlQuery = productSqlQuery + " AND (";
			
			String[] genreArray = genreTag.split(","); 
			List<String> genreList = new ArrayList<>(Arrays.asList(genreArray));
			for(String genre : genreList){
				String formatedGenre = String.format("%02d", Integer.parseInt(genre));
				if (firstSearch)
					firstSearch = false;
				else
					productSqlQuery = productSqlQuery + " OR ";
				productSqlQuery = productSqlQuery + "genre_tags LIKE('%'||'"+formatedGenre+"'||'%')";
			}
			productSqlQuery = productSqlQuery + " )";
		}
		
		if(lang != null) {
			productSqlQuery = productSqlQuery + " AND language = '"+lang.toLowerCase()+"'";
		}
		
		List<Long> productIdList = queryRepository.findByQuery(productSqlQuery)
				.stream().map(product -> {
					Long productId = product.getId();
					return productId;
				}).toList();
		
		Page<ProductEntity> products = repository.findAllByIdIn(productIdList, pageable);
		
		for (ProductEntity product : products) {
			CurrencyResponse currency = currencyClient.getCurrency(product.getPrice(), product.getCurrency(), targetCurrency);
			
			product.setConvertedPrice(currency.getConvertedValue());
			product.setGenreTagsList(tagsController.getTagsListFromString(product.getGenreTagsString()));
			product.setEnviroment("Product-service running on port: " + serverPort + " - " + currency.getEnviroment());
		}
		
		return ResponseEntity.ok(products);
	}
	
	@GetMapping("/query")
	public ResponseEntity<List<Long>> getProductsfromQuery(@RequestParam(required = true) String sqlQuery) {
		List<ProductEntity> productList = queryRepository.findByQuery(sqlQuery);
		List<Long> idList = productList.stream().map(product -> {
			Long productId = product.getId();
			return productId;
		}).toList();
		return ResponseEntity.ok(idList);
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
