package com.br.ecommerce.config;

import com.br.ecommerce.service.notification.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class NotificationConfig {
    
    @Bean
    @Primary // marcando este bean como a implementação principal
        public NotificationService notificationChain() {

        NotificationService base = new BaseNotificationService();
        NotificationService email = new EmailNotificationService(base);
        NotificationService sms = new SmsNotificationService(email);
        
        return sms; //
    }
}