// src/main/java/com/br/ecommerce/domain/observer/CustomerObserver.java
package com.br.ecommerce.domain.observer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomerObserver implements OrderObserver {
    private String name;

        // Injeta o nome do cliente de properties ou usa um valor padrão
    public CustomerObserver(@Value("${customer.observer.name:Cliente Padrão}") String name) {
        this.name = name;
    }
    
    // public CustomerObserver(String name) {
    //     this.name = name;
    // }
    
    @Override
    public void update(String message) {
        System.out.println("Cliente " + name + ": " + message);
    }
}