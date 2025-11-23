package br.edu.atitus.review_service.dtos;

import org.springframework.data.domain.Page;

import br.edu.atitus.review_service.entities.ReviewEntity;

public class ReviewResponseDTO {

	private Page<ReviewEntity> reviews;
	
	private int totalReviews;
	
	private double avgGrade;

	public Page<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(Page<ReviewEntity> reviews) {
		this.reviews = reviews;
	}

	public int getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(int totalReviews) {
		this.totalReviews = totalReviews;
	}

	public double getAvgGrade() {
		return avgGrade;
	}

	public void setAvgGrade(double avgGrade) {
		this.avgGrade = avgGrade;
	}
}
