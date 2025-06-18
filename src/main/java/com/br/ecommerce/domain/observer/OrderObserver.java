// src/main/java/com/br/ecommerce/domain/observer/OrderObserver.java
package com.br.ecommerce.domain.observer;

public interface OrderObserver {
    void update(String message);
}