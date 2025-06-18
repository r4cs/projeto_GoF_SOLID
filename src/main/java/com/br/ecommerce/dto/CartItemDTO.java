package com.br.ecommerce.dto;

import com.br.ecommerce.domain.product.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Product product;
    private int quantity;
    
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}