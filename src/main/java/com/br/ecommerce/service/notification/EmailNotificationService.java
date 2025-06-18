package com.br.ecommerce.service.notification;

// import org.springframework.stereotype.Component;

// @Component
public class EmailNotificationService extends NotificationDecorator {
    
    public EmailNotificationService(NotificationService decoratedService) {
        super(decoratedService);
    }
    
    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("Enviando por email: " + message);
    }
}