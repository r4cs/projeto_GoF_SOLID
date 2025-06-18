// src/main/java/com/br/ecommerce/repository/CatalogComponentRepository.java
package com.br.ecommerce.repository;

import com.br.ecommerce.domain.catalog.CatalogComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogComponentRepository extends JpaRepository<CatalogComponent, Long> {
}