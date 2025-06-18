package com.br.ecommerce.domain.catalog;

import jakarta.persistence.Entity;

@Entity
public class ProductLeaf extends CatalogComponent {
    
    private String name;
    
    @Override
    public void display() {
        System.out.println("Produto: " + name);
    }
    
    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}