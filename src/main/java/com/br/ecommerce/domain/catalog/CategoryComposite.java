package com.br.ecommerce.domain.catalog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class CategoryComposite extends CatalogComponent {
    
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<CatalogComponent> components = new ArrayList<>();
    
    @Override
    public void display() {
        System.out.println("Categoria: " + name);
        for (CatalogComponent component : components) {
            component.display();
        }
    }
}