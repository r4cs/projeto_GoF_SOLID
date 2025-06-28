package com.br.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import com.br.ecommerce.service.AuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/cart/checkout").authenticated() // Adicione esta linha
                        .requestMatchers("/products", "/customer", "/fragments/**", "/cart/**", "/orders/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .permitAll()
                                .defaultSuccessUrl("/")
                                .userInfoEndpoint(userInfo -> userInfo
                                .userService(authService)
                        )
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(false)
                        .clearAuthentication(false)
                );

        return http.build();
        }

        @Bean
                public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/css/**", "/images/**", "/static/images/**",
                "/h2-console/**"
        );
    }
}