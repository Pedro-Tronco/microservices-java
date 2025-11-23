package br.edu.atitus.inventory_service.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import br.edu.atitus.inventory_service.entities.InventoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class QueryInventoryRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public ArrayList<InventoryEntity> findByQuery(String sqlQuery) {
		Query query = entityManager.createNativeQuery(sqlQuery, InventoryEntity.class);
		List<InventoryEntity> list = query.getResultList();
		ArrayList<InventoryEntity> arrayList = new ArrayList<>(list);
		return arrayList;
	}
	
}
