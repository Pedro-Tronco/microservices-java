package br.edu.atitus.cart_service.entities;

import java.util.Objects;

public class CartId {
	
	private Long userId;
	
	private Long productId;
	
	public CartId() {}
	
	public CartId(Long userId, Long productId) {
		this.userId = userId;
		this.productId = productId;
	}
	
	@Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartId)) return false;
        CartId that = (CartId) o;
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
