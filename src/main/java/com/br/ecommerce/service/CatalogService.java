// src/main/java/com/br/ecommerce/service/CatalogService.java
package com.br.ecommerce.service;

import com.br.ecommerce.domain.catalog.CategoryComposite;
import com.br.ecommerce.domain.catalog.ProductLeaf;
import com.br.ecommerce.repository.CatalogComponentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogService {

    private final CatalogComponentRepository repository;

    public CatalogService(CatalogComponentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void createSampleCatalog() {
        CategoryComposite electronics = new CategoryComposite();
        electronics.setName("Eletrônicos");
        
        ProductLeaf tv = new ProductLeaf();
        tv.setName("TV Smart 55\"");
        
        ProductLeaf smartphone = new ProductLeaf();
        smartphone.setName("Smartphone Premium");
        
        electronics.getComponents().add(tv);
        electronics.getComponents().add(smartphone);
        
        repository.save(electronics);
    }
}