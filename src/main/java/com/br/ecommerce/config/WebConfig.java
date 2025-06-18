package com.br.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler(
                "/js/**", 
                "/css/**", 
                "/images/**"
            )
            .addResourceLocations(
                "classpath:/static/js/",
                "classpath:/static/css/", 
                "classpath:/static/images/"
            )
            .setCachePeriod(3600)
            .resourceChain(true);
    }
}