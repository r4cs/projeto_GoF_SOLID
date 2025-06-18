package com.br.ecommerce.service;

import com.br.ecommerce.domain.*;
import com.br.ecommerce.domain.payment.PaymentMethod;
import com.br.ecommerce.domain.product.Product;
import com.br.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@Transactional
public class CartService {
    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private PaymentMethod paymentMethod;
    
    public CartService(OrderService orderService,
                             CustomerRepository customerRepository,
                             ProductRepository productRepository,
                             CartRepository cartRepository) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }
    
    public void setPaymentMethod(PaymentMethod method) {
        this.paymentMethod = method;
    }
    
    public void addItem(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        // Product product = productRepository.findById(productId)
        //     .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        Cart cart = customer.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(customer);
            customer.setCart(cart);
        }
        
        cart.getItems().merge(productId, quantity, Integer::sum);
        cartRepository.save(cart);
    }
    
    public void removeItem(Long customerId, Long productId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Cart cart = customer.getCart();
        if (cart != null) {
            cart.getItems().remove(productId);
            cartRepository.save(cart);
        }
    }
    
    public Map<Long, Integer> getCartItems(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Cart cart = customer.getCart();
        return cart != null ? cart.getItems() : Map.of();
    }
    
    public double getTotal(Long customerId) {
        Map<Long, Integer> items = getCartItems(customerId);
        double total = 0.0;
        
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
            total += product.getPrice() * entry.getValue();
        }
        
        return total;
    }
    
    public Long checkout(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        // Criar pedido a partir do carrinho
        Order order = orderService.createOrderFromCart(customer);
        
        // Processar pagamento
        paymentMethod.pay(getTotal(customerId));
        
        // Limpar carrinho
        Cart cart = customer.getCart();
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
        
        return order.getId();
    }
}

// package com.br.ecommerce.service;

// import com.br.ecommerce.domain.*;
// import com.br.ecommerce.domain.product.Product;
// import com.br.ecommerce.dto.CartItemDTO;
// import com.br.ecommerce.repository.*;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.Objects;
// import java.util.stream.Collectors;

// @Service
// @Transactional
// public class CartService {
//     private final CartRepository cartRepository;
//     private final CustomerRepository customerRepository;
//     private final ProductRepository productRepository;

//     public CartService(CartRepository cartRepository,
//                      CustomerRepository customerRepository,
//                      ProductRepository productRepository) {
//         this.cartRepository = cartRepository;
//         this.customerRepository = customerRepository;
//         this.productRepository = productRepository;
//     }

//     public void addItem(Long customerId, Long productId, int quantity) {
//         Customer customer = customerRepository.findById(customerId)
//             .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
//         Cart cart = customer.getCart();
//         if (cart == null) {
//             cart = new Cart();
//             cart.setCustomer(customer);
//         }
        
//         cart.getItems().merge(productId, quantity, Integer::sum);
//         cartRepository.save(cart);
//     }

//     public void removeItem(Long customerId, Long productId) {
//         Customer customer = customerRepository.findById(customerId)
//             .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
//         Cart cart = customer.getCart();
//         if (cart != null) {
//             cart.getItems().remove(productId);
//             cartRepository.save(cart);
//         }
//     }

//     public List<CartItemDTO> getCartItems(Long customerId) {
//         Customer customer = customerRepository.findById(customerId)
//             .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
//         Cart cart = customer.getCart();
//         if (cart == null || cart.getItems().isEmpty()) {
//             return List.of();
//         }
        
//         return cart.getItems().entrySet().stream()
//             .map(entry -> {
//                 Product product = productRepository.findById(entry.getKey()).orElse(null);
//                 if (product == null) {
//                     removeItem(customerId, entry.getKey()); // Limpa itens inválidos
//                     return null;
//                 }
//                 return new CartItemDTO(product, entry.getValue());
//             })
//             .filter(Objects::nonNull)
//             .collect(Collectors.toList());
//     }

//     public double getTotal(Long customerId) {
//         return getCartItems(customerId).stream()
//             .mapToDouble(CartItemDTO::getSubtotal)
//             .sum();
//     }
// }