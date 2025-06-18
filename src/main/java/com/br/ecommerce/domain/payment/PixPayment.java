// src/main/java/com/br/ecommerce/domain/payment/PixPayment.java
package com.br.ecommerce.domain.payment;

import org.springframework.stereotype.Component;

@Component
public class PixPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando R$" + amount + " via PIX");
    }
}