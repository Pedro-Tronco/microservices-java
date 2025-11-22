package br.edu.atitus.cart_service.entities;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.edu.atitus.cart_service.clients.GenreTagResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@IdClass(CartId.class)
@Table(name = "tb_cart")
public class CartEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @JsonIgnore
    private Long userId;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "is_selected")
    private boolean isSelected;
    
    @Column
    private Timestamp inclusion;
    
    @Transient
    private String title;
	
    @Transient
	private String author;
	
    @Transient
	private String synopsis;
	
    @Transient
	private String language;
	
    @Transient
	private String publisher;
	
    @Transient
	private String fileExtension;
	
    @Transient
	private int pageCount;
	
    @Transient
	private String downloadUrl;
	
    @Transient
	private String imageUrl;
    
    @Transient
    private String genreTagsString;
    
    @Transient
	private List<GenreTagResponse> genreTagsList;
    
    @Transient
    private String enviroment;
    
    @Transient
    private double convertedPrice;
    
    @Transient
    private double price;
    
    @Transient
    private String currency;

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

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Timestamp getInclusion() {
		return inclusion;
	}

	public void setInclusion(Timestamp inclusion) {
		this.inclusion = inclusion;
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

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
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

	public String getGenreTagsString() {
		return genreTagsString;
	}

	public void setGenreTagsString(String genreTagsString) {
		this.genreTagsString = genreTagsString;
	}

	public List<GenreTagResponse> getGenreTagsList() {
		return genreTagsList;
	}

	public void setGenreTagsList(List<GenreTagResponse> genreTagsList) {
		this.genreTagsList = genreTagsList;
	}

	public String getEnviroment() {
		return enviroment;
	}

	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}

	public double getConvertedPrice() {
		return convertedPrice;
	}

	public void setConvertedPrice(double convertedPrice) {
		this.convertedPrice = convertedPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
    
    
}
