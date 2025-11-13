package br.edu.atitus.inventory_service.entities;

import java.io.Serializable;
import java.util.Objects;


public class InventoryId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    
    private Long productId;

    public InventoryId() {}
    
    public InventoryId(Long userId, Long productId) {
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

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryId)) return false;
        InventoryId that = (InventoryId) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}
