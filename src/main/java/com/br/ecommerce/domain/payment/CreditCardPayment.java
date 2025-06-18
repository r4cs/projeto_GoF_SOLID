// src/main/java/com/br/ecommerce/domain/payment/CreditCardPayment.java
package com.br.ecommerce.domain.payment;

import org.springframework.stereotype.Component;

@Component
public class CreditCardPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Pagando R$" + amount + " via Cartão de Crédito");
    }
}