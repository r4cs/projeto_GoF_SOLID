// src/main/java/com/br/ecommerce/repository/CartRepository.java
package com.br.ecommerce.repository;

import com.br.ecommerce.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(Long customerId);
}