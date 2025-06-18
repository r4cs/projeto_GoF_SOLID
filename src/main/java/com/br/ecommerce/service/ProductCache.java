package com.br.ecommerce.service;

import com.br.ecommerce.domain.product.Product;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductCache {
    private final Map<Long, Product> cache = new HashMap<>();
    
    public Optional<Product> get(Long id) {
        return Optional.ofNullable(cache.get(id));
    }
    
    public List<Product> getAll() {
        return new ArrayList<>(cache.values());
    }
    
    public void store(Product product) {
        cache.put(product.getId(), product);
    }
    
    public void storeAll(List<Product> products) {
        products.forEach(this::store);
    }
    
    public void clear() {
        cache.clear();
    }
}