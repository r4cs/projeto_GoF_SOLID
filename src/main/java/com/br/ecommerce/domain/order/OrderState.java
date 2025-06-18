// src/main/java/com/br/ecommerce/domain/order/OrderState.java
package com.br.ecommerce.domain.order;

import com.br.ecommerce.domain.Order;

public interface OrderState {
    void next(Order order);
    void previous(Order order);
    void printStatus();
}