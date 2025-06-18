// src/main/java/com/br/ecommerce/domain/product/ElectronicProduct.java
package com.br.ecommerce.domain.product;

import jakarta.persistence.Entity;

@Entity
public class JewleryProduct extends Product {
    @Override
    public void displayDetails() {
        System.out.println("Joias: " + getTitle() + " - R$" + getPrice());
    }
}