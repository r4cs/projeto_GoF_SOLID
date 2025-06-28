// src/main/java/com/br/ecommerce/dto/OrderRequestDTO.java
package com.br.ecommerce.dto;

import com.br.ecommerce.domain.Customer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private Customer customer;
    private List<OrderItemDTO> items;

}