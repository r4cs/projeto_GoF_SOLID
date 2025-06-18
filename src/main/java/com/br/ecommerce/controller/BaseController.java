package com.br.ecommerce.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public abstract class BaseController {
    
    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(CsrfToken token) {
        return token;
    }
}