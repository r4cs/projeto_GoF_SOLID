// src/main/java/com/br/ecommerce/domain/order/ProcessingOrderState.java
package com.br.ecommerce.domain.order;

import org.springframework.stereotype.Component;

import com.br.ecommerce.domain.Order;

@Component
public class ProcessingOrderState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new ShippedOrderState());
    }
    
    @Override
    public void previous(Order order) {
        order.setState(new NewOrderState());
    }
    
    @Override
    public void printStatus() {
        System.out.println("Pedido em processamento - Preparando para envio");
    }
}