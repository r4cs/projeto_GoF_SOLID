// src/main/java/com/br/ecommerce/external/fakestore/FakeStoreProductAdapter.java
package com.br.ecommerce.external.fakestore;

import org.springframework.stereotype.Component;

import com.br.ecommerce.domain.Product;
import com.br.ecommerce.dto.FakeStoreProductDTO;

@Component
public class FakeStoreProductAdapter {
    
    public Product adaptToProduct(FakeStoreProductDTO dto) {
        Product product = new Product();
        
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());

        if (dto.getDescription() != null && dto.getDescription().length() > 255) {
            product.setDescription(dto.getDescription().substring(0, 240).concat("(...)"));
        } else {
            product.setDescription(dto.getDescription());
        }

        product.setImage(dto.getImage());

        
            
        return product;
    }
    
    public FakeStoreProductDTO adaptToDto(Product product) {
        FakeStoreProductDTO dto = new FakeStoreProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setImage(product.getImage());
        return dto;
    }
}