package com.br.ecommerce.domain.payment;

public interface PaymentMethod {
    void pay(double amount);
}