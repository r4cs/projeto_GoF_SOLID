package com.br.ecommerce.service.notification;

// import org.springframework.stereotype.Component;

// @Component
public class BaseNotificationService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Enviando notificação básica: " + message);
    }
}