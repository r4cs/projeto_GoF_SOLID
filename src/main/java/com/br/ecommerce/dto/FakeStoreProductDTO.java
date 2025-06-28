// src/main/java/com/br/ecommerce/dto/FakeStoreProductDTO.java
package com.br.ecommerce.dto;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FakeStoreProductDTO {
    private Long id;
    private String title;
    private Double price;
    private String category;
    @Length(min=1, max=255)
    private String description;
    private String image;

}