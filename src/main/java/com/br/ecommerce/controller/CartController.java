// src/main/java/com/br/ecommerce/controller/CartController.java
package com.br.ecommerce.controller;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.dto.CartItemDTO;
import com.br.ecommerce.service.CartService;

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
        return "fragments/cart";
    }
    
    @PostMapping("/add/{productId}")
    public String addToCart(
                          @PathVariable Long productId, 
                          @RequestBody CartItemDTO cartItem,
                          @AuthenticationPrincipal Customer customer) {
                            
        
        cartService.addItem(customer.getId(), cartItem.getProduct().getId(), cartItem.getQuantity());
        return "redirect:/products";
    }
    
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId,
                               @AuthenticationPrincipal Customer customer) {
        cartService.removeItem(customer.getId(), productId);
        return "redirect:/cart";
    }
    
    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal Customer customer) {
        Long orderId = cartService.checkout(customer.getId());
        return "redirect:/orders/" + orderId;
    }
}