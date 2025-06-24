// src/main/java/com/br/ecommerce/controller/CartController.java
package com.br.ecommerce.controller;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.dto.CartItemDTO;
import com.br.ecommerce.service.CartService;

import org.springframework.http.HttpStatus;
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
        model.addAttribute("cartItems", cartService.getCartItems(customer.getId()));
        model.addAttribute("total", cartService.getTotal(customer.getId()));
        model.addAttribute("activeFragment", "cart");
        return "home/homeSignedIn";
    }

    @PostMapping("/add/{productId}")
    @ResponseBody
    public ResponseEntity<?> addToCart(
        @PathVariable Long productId,
        @RequestBody CartItemDTO cartItem,
        @AuthenticationPrincipal Customer customer) {
        
        try {
            cartService.addItem(customer.getId(), productId, cartItem.getQuantity());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PostMapping("/checkout")
    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal Customer customer) {
        Long orderId = cartService.checkout(customer.getId());
        return "redirect:/orders/" + orderId;
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointer(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Dados inválidos no carrinho: " + ex.getMessage());
    }
}