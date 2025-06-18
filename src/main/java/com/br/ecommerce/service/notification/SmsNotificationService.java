package com.br.ecommerce.service.notification;

// import org.springframework.stereotype.Component;

// @Component
public class SmsNotificationService extends NotificationDecorator {
    
    public SmsNotificationService(NotificationService decoratedService) {
        super(decoratedService);
    }
    
    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("Enviando por SMS: " + message);
    }
}