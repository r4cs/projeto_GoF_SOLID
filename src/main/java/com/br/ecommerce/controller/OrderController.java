// src/main/java/com/br/ecommerce/controller/OrderController.java
package com.br.ecommerce.controller;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.domain.Order;
import com.br.ecommerce.domain.order.OrderUtils;
import com.br.ecommerce.dto.OrderRequestDTO;
import com.br.ecommerce.service.OrderService;

import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public String listOrders(Model model, @AuthenticationPrincipal Customer customer) {
        List<Order> orders = orderService.getOrdersByCustomer(customer.getId());
        System.out.println("\n\nCustomer ID: " + customer.getId());
        System.out.println("Número de pedidos encontrados: " + orders.size());
        System.out.println("Orders found: " + orders + "\n\n");
        model.addAttribute("orders", orders);
        model.addAttribute("activeFragment", "orders");
        return "home/homeSignedIn";
    }
    
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model, 
                          @AuthenticationPrincipal Customer customer) throws AccessDeniedException {
        Order order = orderService.getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Acesso negado");
        }
        
        model.addAttribute("order", order);
        model.addAttribute("activeFragment", "detail");
        return "home/homeSignedIn";
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }
    
    @PostMapping("/{id}/advance")
    public ResponseEntity<Void> advanceOrderStatus(@PathVariable Long id) {
        orderService.advanceOrderStatus(id);
        return ResponseEntity.ok().build();
        // return "redirect:/orders/track/" + id;
    }

    @GetMapping("/track/{id}")
    public String trackOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        model.addAttribute("activeFragment", order);
        return "order-status";
    }

    @ModelAttribute("orderUtils")
    public OrderUtils orderUtils() {
        return new OrderUtils();
    }
}