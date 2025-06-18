// src/main/java/com/br/ecommerce/domain/order/DeliveredOrderState.java
package com.br.ecommerce.domain.order;

import org.springframework.stereotype.Component;

import com.br.ecommerce.domain.Order;

@Component
public class DeliveredOrderState implements OrderState {
    @Override
    public void next(Order order) {
        System.out.println("Pedido já foi entregue");
    }
    
    @Override
    public void previous(Order order) {
        order.setState(new ShippedOrderState());
    }
    
    @Override
    public void printStatus() {
        System.out.println("Pedido entregue - Finalizado");
    }
}