// src/main/java/com/br/ecommerce/domain/Customer.java
package com.br.ecommerce.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.br.ecommerce.config.MapToJsonConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name="customers")
public class Customer implements UserDetails, OAuth2User {
    @Id
    private Long id;
    private String name;
    private String email;
    private String activeFragment;
    private String password;

    // Atributos do OAuth2User, usados pelo Spring Security para acessar info adicional
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> attributes; 
    
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    // Construtor para usar no OAuth2UserService, para criar o Customer a partir dos atributos do OAuth2
    public Customer(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.id = Long.valueOf(attributes.get("id").toString());
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
        this.password = null; 
    }

    // --- Implementação dos métodos de UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // todos os usuários logados têm a role "ROLE_USER"
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }


    @Override
    public String getUsername() {
        return this.email; // Ou String.valueOf(this.id) se preferir o ID como username
    }

        // Implemente esses métodos para gerenciar o status da conta.
    @Override
    public boolean isAccountNonExpired() { return true; } // Ajuste conforme sua lógica
    @Override
    public boolean isAccountNonLocked() { return true; } // Ajuste conforme sua lógica
    @Override
    public boolean isCredentialsNonExpired() { return true; } // Ajuste conforme sua lógica
    @Override
    public boolean isEnabled() { return true; } // Ajuste conforme sua lógica

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        // Retorne o identificador único do usuário no contexto do OAuth2.
        // O GitHub geralmente usa "id" como o identificador primário.
        return String.valueOf(this.id); // Ou (String) attributes.get("login");
    }

}