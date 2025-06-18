// src/main/java/com/br/ecommerce/domain/order/NewOrderState.java
package com.br.ecommerce.domain.order;

import com.br.ecommerce.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class NewOrderState implements OrderState {
    @Override
    public void next(Order order) {
        order.setState(new ProcessingOrderState());
    }
    
    @Override
    public void previous(Order order) {
        System.out.println("O pedido está no estado inicial.");
    }
    
    @Override
    public void printStatus() {
        System.out.println("Pedido novo - Aguardando processamento");
    }
}

// Adicione similarmente as outras classes de estado (ProcessingOrderState, ShippedOrderState, DeliveredOrderState)

// // src/main/java/com/br/ecommerce/domain/order/NewOrderState.java
// package com.br.ecommerce.domain.order;

// import org.springframework.stereotype.Component;

// import com.br.ecommerce.domain.Order;

// @Component
// public class NewOrderState implements OrderState {
//     @Override
//     public void next(Order order) {
//         order.setState(new ProcessingOrderState());
//     }
    
//     @Override
//     public void previous(Order order) {
//         System.out.println("O pedido está no estado inicial.");
//     }
    
//     @Override
//     public void printStatus() {
//         System.out.println("Pedido novo - Aguardando processamento");
//     }
// }