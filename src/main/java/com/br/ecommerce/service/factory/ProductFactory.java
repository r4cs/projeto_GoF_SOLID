// src/main/java/com/br/ecommerce/service/factory/ProductFactory.java
package com.br.ecommerce.service.factory;

import com.br.ecommerce.domain.product.Product;

public interface ProductFactory {
    Product createProduct(String title, double price);
}