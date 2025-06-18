package com.br.ecommerce.dto;

// import com.br.ecommerce.domain.product.Product;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    // private Product product;
    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;

    @Positive
    private double unitPrice;

    public double getSubtotal() {
        return quantity * unitPrice;
    }
    
    // public double getSubtotal() {
    //     return product.getPrice() * quantity;
    // }
}