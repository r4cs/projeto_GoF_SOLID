// src/main/java/com/br/ecommerce/domain/Order.java
package com.br.ecommerce.domain;

import com.br.ecommerce.domain.order.OrderState;
import com.br.ecommerce.domain.observer.OrderObserver;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Customer customer;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    private LocalDateTime orderDate;
    
    private OrderStatus status;
    
    @Transient
    private OrderState state;
    
    @Transient
    private List<OrderObserver> observers = new ArrayList<>();
    
    public void setState(OrderState state) {
        this.state = state;
        notifyObservers();
    }
    
    public void nextState() {
        state.next(this);
    }
    
    public void previousState() {
        state.previous(this);
    }
    
    public void printStatus() {
        state.printStatus();
    }
    
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.update("Status do pedido atualizado: " + state.getClass().getSimpleName());
        }
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public Double getTotalPrice() {
    return items.stream()
        .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
        .sum();
    }
}