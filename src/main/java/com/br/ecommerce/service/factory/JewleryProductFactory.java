// src/main/java/com/br/ecommerce/service/factory/ElectronicProductFactory.java
package com.br.ecommerce.service.factory;

import com.br.ecommerce.domain.product.JewleryProduct;
import com.br.ecommerce.domain.product.Product;
import org.springframework.stereotype.Component;

@Component
public class JewleryProductFactory implements ProductFactory {
    @Override
    public Product createProduct(String title, double price) {
        JewleryProduct product = new JewleryProduct();
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory("Jewlery");
        return product;
    }
}