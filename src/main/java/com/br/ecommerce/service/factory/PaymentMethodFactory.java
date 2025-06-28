package com.br.ecommerce.service.factory;

import com.br.ecommerce.domain.payment.BoletoPayment;
import com.br.ecommerce.domain.payment.CreditCardPayment;
import com.br.ecommerce.domain.payment.PaymentMethod;
import com.br.ecommerce.domain.payment.PixPayment;

public class PaymentMethodFactory {
    public static PaymentMethod createPaymentMethod(String type) {
        return switch (type) {
            case "PIX" -> new PixPayment();
            case "BOLETO" -> new BoletoPayment();
            case "CREDIT_CARD" -> new CreditCardPayment();
            default -> throw new IllegalArgumentException("Método de pagamento inválido");
        };
    }
}