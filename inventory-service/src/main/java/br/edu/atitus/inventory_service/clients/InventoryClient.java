package br.edu.atitus.inventory_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

	@PutMapping("/ws/inventory/bookmarks/remove-all/{bookmarkId}")
	public ResponseEntity<String> removeAllBookmarkIdFromItems(
			@PathVariable Long bookmarkId,
			@RequestHeader("X-User-Id") Long userId);
}
