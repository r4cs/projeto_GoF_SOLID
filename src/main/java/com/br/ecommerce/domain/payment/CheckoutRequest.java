package com.br.ecommerce.domain.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private String paymentMethod;
}