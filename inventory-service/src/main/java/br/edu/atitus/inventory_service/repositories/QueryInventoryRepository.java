package br.edu.atitus.inventory_service.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.edu.atitus.inventory_service.entities.InventoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class QueryInventoryRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public List<InventoryEntity> findByQuery(String sqlQuery, Pageable pageable) {
		Query query = entityManager.createNativeQuery(sqlQuery, InventoryEntity.class);
		List<InventoryEntity> list = query.getResultList();
		return list;
	}
	
}
