package br.edu.atitus.inventory_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(InventoryId.class)
@Table(name = "tb_inventory")
public class InventoryEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    @Column(name = "bookmarks")
    private String bookmarks;

    public InventoryEntity() {}
    
    public InventoryEntity(Long userId, Long productId, boolean isFavorite, String bookmarks) {
        this.userId = userId;
        this.productId = productId;
    }

    
    
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public String getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(String bookmarks) {
		this.bookmarks = bookmarks;
	}
	
	
}
