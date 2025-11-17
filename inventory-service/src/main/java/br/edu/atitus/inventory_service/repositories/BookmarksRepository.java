package br.edu.atitus.inventory_service.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.atitus.inventory_service.entities.BookmarksEntity;
import br.edu.atitus.inventory_service.entities.BookmarksId;
import jakarta.transaction.Transactional;

public interface BookmarksRepository extends JpaRepository<BookmarksEntity, BookmarksId>{

	List<BookmarksEntity> findByUserId(Long userId);
	
	BookmarksEntity findByUserIdAndBookmarkId(Long userId, UUID bookmarkId);
	
	List<BookmarksEntity> findAllByUserIdAndBookmarkIdIn(Long userId, List<UUID> bookmarkId);
	
	@Transactional
	void deleteByUserIdAndBookmarkId(Long userId, UUID bookmarkId);
}
