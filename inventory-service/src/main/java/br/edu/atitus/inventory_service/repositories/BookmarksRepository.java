package br.edu.atitus.inventory_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.atitus.inventory_service.entities.BookmarksEntity;
import br.edu.atitus.inventory_service.entities.BookmarksId;
import jakarta.transaction.Transactional;

public interface BookmarksRepository extends JpaRepository<BookmarksEntity, BookmarksId>{

	List<BookmarksEntity> findByUserId(Long userId);
	
	BookmarksEntity findByUserIdAndBookmarkId(Long userId, Long bookmarkId);
	
	List<BookmarksEntity> findAllByUserIdAndBookmarkIdIn(Long userId, List<Long> bookmarkId);
	
	@Query(value = "SELECT COALESCE(MAX(bookmarkId), 0) FROM BookmarksEntity WHERE userId = :userId")
	Long findMaxBookmarkIdByUserId(Long userId);
	
	@Transactional
	void deleteByUserIdAndBookmarkId(Long userId, Long bookmarkId);
}
