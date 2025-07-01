// src/main/java/com/br/ecommerce/controller/CartController.java
package com.br.ecommerce.controller;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.domain.payment.BoletoPayment;
import com.br.ecommerce.domain.payment.CreditCardPayment;
import com.br.ecommerce.domain.payment.PaymentMethod;
import com.br.ecommerce.domain.payment.PixPayment;
import com.br.ecommerce.dto.OrderItemDTO;
import com.br.ecommerce.service.CartService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping
    public String viewCart(Model model, @AuthenticationPrincipal Customer customer) {
        model.addAttribute("orderItems", cartService.getOrderItems(customer.getId()));
        model.addAttribute("total", cartService.getTotal(customer.getId()));
        model.addAttribute("activeFragment", "cart");
        return "home/homeSignedIn";
    }

    @PostMapping("/add/{productId}")
    @ResponseBody
    public ResponseEntity<?> addToCart(
        @PathVariable Long productId,
        @RequestBody OrderItemDTO orderItem,
        @AuthenticationPrincipal Customer customer) {
        
        try {
            cartService.addItem(customer.getId(), productId, orderItem.getQuantity());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/checkout", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> checkout(
        @AuthenticationPrincipal Customer customer,
        @RequestBody Map<String, String> request) {
        
        try {
            // método de pagamento básico
            PaymentMethod paymentMethod;
            switch(request.get("paymentMethod")) {
                case "PIX": paymentMethod = new PixPayment(); break;
                case "BOLETO": paymentMethod = new BoletoPayment(); break;
                default: paymentMethod = new CreditCardPayment();
            }
            
            cartService.setPaymentMethod(paymentMethod);
            Long orderId = cartService.checkout(customer.getId());

            System.out.println("\nOrder id em cart controller: " + orderId);
            
            return ResponseEntity.ok(Map.of("orderId", orderId));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeItem(
        @AuthenticationPrincipal Customer customer,
        @PathVariable Long productId
    ) {
        try {
            cartService.removeItem(customer.getId(), productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateItemQuantity(
        @AuthenticationPrincipal Customer customer,
        @PathVariable Long productId,
        @RequestBody Map<String, Integer> request
    ) {
        try {
            cartService.updateItemQuantity(customer.getId(), productId, request.get("quantity"));
            double newTotal = cartService.getTotal(customer.getId());
            return ResponseEntity.ok(Map.of("total", newTotal));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointer(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Dados inválidos no carrinho: " + ex.getMessage());
    }
}