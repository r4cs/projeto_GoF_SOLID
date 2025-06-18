// src/main/java/com/br/ecommerce/dto/CartDTO.java
package com.br.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CartDTO {
    private Map<Long, Integer> items; // productId -> quantity
    private double total;
}