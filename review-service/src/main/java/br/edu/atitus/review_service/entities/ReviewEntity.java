package br.edu.atitus.review_service.entities;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@IdClass(ReviewId.class)
@Table(name = "tb_review")
public class ReviewEntity {

	@Id
    @Column(name = "user_id", nullable = false)
    @JsonIgnore
    private Long userId;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column
    private int grade;
    
    @Column
    private String title;
    
    @Column
    private String comment;

    @Column(name = "post_date")
    private Timestamp postDate;
    
    @Transient
    private String username;

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

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	
}
