package br.edu.atitus.inventory_service.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.inventory_service.dtos.BookmarkDTO;
import br.edu.atitus.inventory_service.dtos.InventoryListDTO;
import br.edu.atitus.inventory_service.entities.BookmarksEntity;
import br.edu.atitus.inventory_service.repositories.BookmarksRepository;

@RestController
@RequestMapping("/ws/inventory/bookmarks")
public class BookmarksController {
	
	private final BookmarksRepository repository;
	
	public BookmarksController(BookmarksRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/{bookmarksString}")
	public List<BookmarksEntity> findBookmarksByBookmarkString(
			@PathVariable String bookmarksString,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) {
		String[] bookmarksStringArray = bookmarksString.split(",");
		UUID[] bookmarksLongArray = Arrays.stream(bookmarksStringArray)
				.map(UUID::fromString)
				.toArray(UUID[]::new);
		List<UUID> bookmarksLongList = new ArrayList<>(Arrays.asList(bookmarksLongArray));
		List<BookmarksEntity> bookmarksList = repository.findAllByUserIdAndBookmarkIdIn(userId, bookmarksLongList);
		return bookmarksList;
	}
	
	@GetMapping
	public ResponseEntity<List<BookmarksEntity>> findBookmarksByUser(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) throws Exception {
		List<BookmarksEntity> list = repository.findByUserId(userId);
		if(list.isEmpty())
			throw new Exception("Usuário não possui bookmarks");
		return ResponseEntity.ok(list);
	}
	
	@PostMapping
	public ResponseEntity<BookmarksEntity> createBookmark(
			@RequestBody BookmarkDTO dto,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) {
		BookmarksEntity bookmark = new BookmarksEntity();
		UUID uuid = UUID.randomUUID();
		bookmark.setBookmarkId(uuid);
		bookmark.setUserId(userId);
		BeanUtils.copyProperties(dto, bookmark);
		repository.save(bookmark);
		return ResponseEntity.status(201).body(bookmark);
	}
	
	@PutMapping("/{bookmarkId}")
	public ResponseEntity<BookmarksEntity> updateBookmark(
			@RequestBody BookmarkDTO dto,
			@PathVariable UUID bookmarkId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) throws Exception {
		var bookmark = repository.findByUserIdAndBookmarkId(userId, bookmarkId);
		if(bookmark == null)
			throw new Exception("Bookmark not found");
		BeanUtils.copyProperties(dto, bookmark);
		repository.save(bookmark);
		return ResponseEntity.status(200).body(bookmark);
	}
		
	@DeleteMapping("/{bookmarkId}")
	public ResponseEntity<String> deleteBookmark(
			@PathVariable UUID bookmarkId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) throws Exception {
		repository.deleteByUserIdAndBookmarkId(userId, bookmarkId);
		return ResponseEntity.status(200).body("Item apagado com sucesso");
	}
		
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handlerAuth(Exception e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(404).body(message);
	}
}
