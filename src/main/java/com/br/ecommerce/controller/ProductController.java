package com.br.ecommerce.controller;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.domain.product.Product;
import com.br.ecommerce.service.ProductFacade;
import com.br.ecommerce.service.CartService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.web.csrf.CsrfToken;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductFacade productFacade;
    public static final int DEFAULT_PAGE_SIZE = 5;
    private final CartService cartService;

    public ProductController(ProductFacade productFacade, CartService cartService) {
        this.productFacade = productFacade;
        this.cartService = cartService;
    }

    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "#{T(java.lang.Integer).toString(T(com.br.ecommerce.controller.ProductController).DEFAULT_PAGE_SIZE)}") int size,
            @RequestParam(required = false) String category,
            Model model,
            CsrfToken csrfToken) {

        // Garantir que o tamanho da página seja razoável
        size = size <= 0 ? DEFAULT_PAGE_SIZE : Math.min(size, 24);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productFacade.getProducts(pageable, category);
        
        // Adicionar atributos para a view
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", productFacade.getAllCategories());
        model.addAttribute("selectedCategory", category);

        model.addAttribute("activeFragment", "products");
        model.addAttribute("_csrf", csrfToken);

        return "home/homeSignedIn";
    }

    @PostMapping("/{productId}/add-to-cart")
    public String addToCart(@PathVariable Long productId,
                        @RequestParam int quantity,
                        @AuthenticationPrincipal Customer customer) {
        cartService.addItem(customer.getId(), productId, quantity);
        return "redirect:/products";
    }
}