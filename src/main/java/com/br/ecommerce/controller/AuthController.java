package com.br.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.br.ecommerce.service.AuthService;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping({"/", "/home"})
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("activeFragment", "customer"); 
            return "home/homeSignedIn";
        } else {
            return "home/homeNotSignedIn";
        }
    }

    @GetMapping("/oauth2/callback/github")
    public String githubCallback(OAuth2UserRequest oauthUser) {
        authService.loadUser(oauthUser);
        return "redirect:/";
    }
}