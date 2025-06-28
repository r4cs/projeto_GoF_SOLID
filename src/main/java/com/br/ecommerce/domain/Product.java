
// src/main/java/com/br/ecommerce/domain/product/Product.java
package com.br.ecommerce.domain;

import com.br.ecommerce.dto.FakeStoreProductDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity(name="products")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// public abstract class Product {
public class Product {
    @Id
    private Long id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;
    private String activeFragment;

    public Product(FakeStoreProductDTO dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.category = dto.getCategory();
        this.description = dto.getDescription();
        this.image = dto.getImage();
        this.activeFragment = "";
    }
}