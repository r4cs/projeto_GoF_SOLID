// src/main/java/com/br/ecommerce/dto/CartResponse.java
package com.br.ecommerce.dto;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartResponseDTO {
    private final Map<Long, Integer> cartItems; 
    private final double totalPrice;
}