// src/main/java/com/br/ecommerce/repository/OrderRepository.java
package com.br.ecommerce.repository;

import com.br.ecommerce.domain.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
}