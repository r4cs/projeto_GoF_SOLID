// src/main/java/com/br/ecommerce/controller/OrderViewController.java
package com.br.ecommerce.controller;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.domain.Order;
import com.br.ecommerce.service.OrderService;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderViewController {
    
    private final OrderService orderService;
    
    public OrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping("/track/{id}")
    public String trackOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        model.addAttribute("order", order);
        model.addAttribute("state", order.getState());
        return "order-status";
    }
    
    @PostMapping("/{id}/advance")
    public String advanceOrder(@PathVariable Long id) {
        orderService.advanceOrderStatus(id);
        return "redirect:/orders/track/" + id;
    }

    @GetMapping
    public String listOrders(Model model, @AuthenticationPrincipal Customer customer) {
        List<Order> orders = orderService.getOrdersByCustomer(customer.getId());
        model.addAttribute("orders", orders);
        return "orders/list";
    }
    
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, 
                          @AuthenticationPrincipal Customer customer) {
        Order order = orderService.getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Acesso negado");
        }
        
        model.addAttribute("order", order);
        return "orders/detail";
    }
}