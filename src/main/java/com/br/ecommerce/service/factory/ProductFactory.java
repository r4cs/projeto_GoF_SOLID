// src/main/java/com/br/ecommerce/service/factory/ProductFactory.java
package com.br.ecommerce.service.factory;

import com.br.ecommerce.domain.Product;
import com.br.ecommerce.dto.FakeStoreProductDTO;

public interface ProductFactory {
    Product createProduct(FakeStoreProductDTO productDTO);
    Product createProduct(String title,
                            double price, 
                            String category, 
                            String description, 
                            String image);
}