// src/main/java/com/br/ecommerce/service/notification/NotificationDecorator.java
package com.br.ecommerce.service.notification;

public abstract class NotificationDecorator implements NotificationService {
    protected final NotificationService decoratedService;
    
    public NotificationDecorator(NotificationService decoratedService) {
        this.decoratedService = decoratedService;
    }
    
    @Override
    public void send(String message) {
        decoratedService.send(message);
    }
}