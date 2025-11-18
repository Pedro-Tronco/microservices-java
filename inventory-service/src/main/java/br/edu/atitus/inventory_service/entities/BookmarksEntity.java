package br.edu.atitus.inventory_service.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(BookmarksId.class)
@Table(name = "tb_bookmarks")
public class BookmarksEntity {

	@Id
	@Column(name = "bookmark_id")
	private Long bookmarkId;
	
	@Id
	@Column(name = "user_id")
	private Long userId;
	
	@Column(length = 50)
	private String description;
	
	@Column(length = 7, name = "hex_color")
	private String hexColor;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBookmarkId() {
		return bookmarkId;
	}

	public void setBookmarkId(Long bookmarkId) {
		this.bookmarkId = bookmarkId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}
	
}
