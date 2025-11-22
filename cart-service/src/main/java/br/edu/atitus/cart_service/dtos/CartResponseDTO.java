package br.edu.atitus.cart_service.dtos;

import org.springframework.data.domain.Page;

import br.edu.atitus.cart_service.entities.CartEntity;

public class CartResponseDTO {

	private Page<CartEntity> items;
	
	private double totalValue;

	public Page<CartEntity> getItems() {
		return items;
	}

	public void setItems(Page<CartEntity> items) {
		this.items = items;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}
	
}
