// src/main/java/com/br/ecommerce/dto/FakeStoreProductDTO.java
package com.br.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDTO {
    private Long id;
    private String title;
    private Double price;
    private String category;
    private String description;
    private String image;
}