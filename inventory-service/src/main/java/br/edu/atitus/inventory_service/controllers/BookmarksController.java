package br.edu.atitus.inventory_service.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
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

import br.edu.atitus.inventory_service.clients.InventoryClient;
import br.edu.atitus.inventory_service.dtos.BookmarkDTO;
import br.edu.atitus.inventory_service.entities.BookmarksEntity;
import br.edu.atitus.inventory_service.repositories.BookmarksRepository;
import jakarta.ws.rs.NotFoundException;

@RestController
@RequestMapping("/ws/inventory/bookmarks")
public class BookmarksController {
	
	private final BookmarksRepository repository;
	private final InventoryClient inventoryClient;
	
	public BookmarksController(BookmarksRepository repository, InventoryClient inventoryClient ) {
		this.repository = repository;
		this.inventoryClient  = inventoryClient;
	}
	
	@GetMapping("/{bookmarksString}")
	public List<BookmarksEntity> findBookmarksByBookmarkString(
			@PathVariable String bookmarksString,
			@RequestHeader("X-User-Id") Long userId) {
		String[] bookmarksStringArray = bookmarksString.split(",");
		Long[] bookmarksLongArray = Arrays.stream(bookmarksStringArray)
				.map(Long::parseLong)
				.toArray(Long[]::new);
		List<Long> bookmarksLongList = new ArrayList<>(Arrays.asList(bookmarksLongArray));
		List<BookmarksEntity> bookmarksList = repository.findAllByUserIdAndBookmarkIdIn(userId, bookmarksLongList);
		return bookmarksList;
	}
	
	@GetMapping
	public ResponseEntity<List<BookmarksEntity>> findBookmarksByUser(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) {
		List<BookmarksEntity> list = repository.findByUserId(userId);
		return ResponseEntity.ok(list);
	}
	
	@PostMapping
	public ResponseEntity<BookmarksEntity> createBookmark(
			@RequestBody BookmarkDTO dto,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) throws Exception {
		BookmarksEntity bookmark = new BookmarksEntity();
		Long id = repository.findMaxBookmarkIdByUserId(userId) + 1;
		if(id > 99)
			throw new Exception("User cannot create any more Bookmarks");
		bookmark.setBookmarkId(id);
		bookmark.setUserId(userId);
		BeanUtils.copyProperties(dto, bookmark);
		repository.save(bookmark);
		return ResponseEntity.status(201).body(bookmark);
	}
	
	@PutMapping("/{bookmarkId}")
	public ResponseEntity<BookmarksEntity> updateBookmark(
			@RequestBody BookmarkDTO dto,
			@PathVariable Long bookmarkId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) {
		var bookmark = repository.findByUserIdAndBookmarkId(userId, bookmarkId);
		if(bookmark == null)
			throw new NotFoundException("Bookmark not found");
		BeanUtils.copyProperties(dto, bookmark);
		repository.save(bookmark);
		return ResponseEntity.status(200).body(bookmark);
	}
		
	@DeleteMapping("/{bookmarkId}")
	public ResponseEntity<String> deleteBookmark(
			@PathVariable Long bookmarkId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Email") String userEmail,
			@RequestHeader("X-User-Type") Integer userType) throws Exception {
		inventoryClient.removeAllBookmarkIdFromItems(bookmarkId, userId, userEmail, userType);
		repository.deleteByUserIdAndBookmarkId(userId, bookmarkId);
		return ResponseEntity.status(200).body("Item apagado com sucesso");
	}
		
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handlerAuth(NotFoundException e) {
		String message = e.getMessage().replaceAll("[\\r\\n]", "");
		return ResponseEntity.status(404).body(message);
	}
}
