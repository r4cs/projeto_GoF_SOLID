// src/main/java/com/br/ecommerce/domain/product/ClothingProduct.java
package com.br.ecommerce.domain.product;

import jakarta.persistence.Entity;

@Entity
public class ClothingProduct extends Product {
    @Override
    public void displayDetails() {
        System.out.println("Vestuário: " + getTitle() + " - R$" + getPrice());
    }
}