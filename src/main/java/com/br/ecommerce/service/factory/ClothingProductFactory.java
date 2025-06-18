// src/main/java/com/br/ecommerce/service/factory/ClothingProductFactory.java
package com.br.ecommerce.service.factory;

import com.br.ecommerce.domain.product.ClothingProduct;
import com.br.ecommerce.domain.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ClothingProductFactory implements ProductFactory {
    @Override
    public Product createProduct(String title, double price) {
        ClothingProduct product = new ClothingProduct();
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory("Clothing");
        return product;
    }
}