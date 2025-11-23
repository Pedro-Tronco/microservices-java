package br.edu.atitus.review_service.entities;

import java.util.Objects;


public class ReviewId {

	private Long userId;
	
	private Long productId;
	
	public ReviewId () {}
	
	public ReviewId(Long userId, Long productId) {
		this.userId = userId;
		this.productId = productId;
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewId)) return false;
        ReviewId that = (ReviewId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
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
	
}
