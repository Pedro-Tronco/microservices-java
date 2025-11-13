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

    private String title;
    
    private String author;
    
    private String synopsis;
    
    private String language;
    
    private String publisher;
    
    @Column(name = "file_extension")
    private String fileExtension;
    
    @Column(name = "genre_tags")
    private String genreTags;
    
    @Column(name = "page_count")
    private int pageCount;
    
    @Column(name = "download_url")
    private String downloadUrl;
    
    @Column(name = "image_url")
    private String imageUrl;
    
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getfileExtension() {
		return fileExtension;
	}

	public void setfileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getGenreTags() {
		return genreTags;
	}

	public void setGenreTags(String genreTags) {
		this.genreTags = genreTags;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
