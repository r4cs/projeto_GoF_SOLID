package com.br.ecommerce.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.ecommerce.domain.Customer;


@Controller
@RequestMapping("/customer")
public class CustomerController {

    @GetMapping
    public String profile(Model model, @AuthenticationPrincipal Customer customer) {
        model.addAttribute("activeFragment", "customer");
        model.addAttribute("customer", customer);
        return "home/homeSignedIn";
    }
    
}