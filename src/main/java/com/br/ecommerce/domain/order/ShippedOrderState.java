// src/main/java/com/br/ecommerce/domain/order/ShippedOrderState.java
package com.br.ecommerce.domain.order;

import org.springframework.stereotype.Component;

import com.br.ecommerce.domain.Order;

@Component
public class ShippedOrderState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new DeliveredOrderState());
    }
    
    @Override
    public void previous(Order order) {
        order.setState(new ProcessingOrderState());
    }
    
    @Override
    public void printStatus() {
        System.out.println("Pedido enviado - A caminho do cliente");
    }
}