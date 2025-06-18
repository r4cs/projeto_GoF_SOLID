package com.br.ecommerce.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private Customer customer;
    
    @ElementCollection
    @CollectionTable(name = "cart_items_mapping", 
        joinColumns = {@JoinColumn(name = "cart_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "productId")
    @Column(name = "quantity")
    private Map<Long, Integer> items = new HashMap<>();
}