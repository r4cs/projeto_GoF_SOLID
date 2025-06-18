// src/main/java/com/br/ecommerce/external/fakestore/FakeStoreProductAdapter.java
package com.br.ecommerce.external.fakestore;

import org.springframework.stereotype.Component;

import com.br.ecommerce.domain.product.ClothingProduct;
import com.br.ecommerce.domain.product.JewleryProduct;
import com.br.ecommerce.domain.product.Product;
import com.br.ecommerce.dto.FakeStoreProductDTO;

@Component
public class FakeStoreProductAdapter {
    
    public Product adapt(FakeStoreProductDTO dto) {
        Product product;
        
        if (dto.getCategory().contains("clothing")) {
            product = new ClothingProduct();
        } else {
            product = new JewleryProduct();
        }
        
        product.setId(dto.getId());
        product.setTitle(dto.getTitle());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
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