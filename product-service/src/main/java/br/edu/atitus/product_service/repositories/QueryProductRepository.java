package br.edu.atitus.product_service.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.atitus.product_service.entities.ProductEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class QueryProductRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<ProductEntity> findByQuery(String sqlQuery) {
		Query query = entityManager.createNativeQuery(sqlQuery, ProductEntity.class);
		return query.getResultList();
	}
	
}
