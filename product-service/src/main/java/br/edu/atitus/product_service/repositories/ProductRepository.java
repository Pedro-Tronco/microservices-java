package br.edu.atitus.product_service.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.atitus.product_service.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

	Page<ProductEntity> findAllByIdIn(List<Long> idList, Pageable pageable);
}
