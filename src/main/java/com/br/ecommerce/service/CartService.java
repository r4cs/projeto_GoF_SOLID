package com.br.ecommerce.service;

import com.br.ecommerce.domain.*;
import com.br.ecommerce.domain.payment.PaymentMethod;
import com.br.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class CartService {
    private final OrderService orderService;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductFacade productFacade;
    private PaymentMethod paymentMethod;
    
    public CartService(OrderService orderService,
                             CustomerRepository customerRepository,
                             CartRepository cartRepository,
                             ProductFacade productFacade) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.productFacade = productFacade;
    }
    
    public void setPaymentMethod(PaymentMethod method) {
        this.paymentMethod = method;
    }

    public void addItem(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        productFacade.getProductById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
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
    
    public Map<Long, Integer> getOrderItems(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Cart cart = customer.getCart();
        return cart != null ? cart.getItems() : Map.of();
    }
    
    public double getTotal(Long customerId) {
        Map<Long, Integer> items = getOrderItems(customerId);
        double total = 0.0;
        
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Product product = productFacade.getProductById(entry.getKey())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
            total += product.getPrice() * entry.getValue();
        }
        
        return total;
    }
    
    public Long checkout(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        if (paymentMethod == null) {
            throw new IllegalStateException("Método de pagamento não selecionado");
        }
        
        Order order = orderService.createOrderFromCart(customer);
        System.out.println("Order: " + order.toString());
        System.out.println("Order id em cart service: " + order.getId());
        System.out.println("Order valor total: " + order.getTotalPrice());

        System.out.println("\nCart Service:");
        for (OrderItem orderItem : order.getItems()) {
            System.out.println("orderItem id: " + orderItem.getId());
            System.out.println("orderItem unit price: " + orderItem.getUnitPrice());
            System.out.println("orderItem quantity: " + orderItem.getQuantity());
        }

        // Processar pagamento *** versao super simplificada ***
        paymentMethod.pay(getTotal(customerId));
        
        clearCart(customerId);
        
        return order.getId();
    }

    public void clearCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Cart cart = customer.getCart();
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    public boolean isCartEmpty(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Cart cart = customer.getCart();
        return cart == null || cart.getItems().isEmpty();
    }

    public void updateItemQuantity(Long customerId, Long productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("A quantidade deve ser pelo menos 1");
        }

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        productFacade.getProductById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        Cart cart = customer.getCart();
        if (cart == null) {
            throw new IllegalStateException("Carrinho não encontrado para este cliente");
        }
        
        if (cart.getItems().containsKey(productId)) {
            cart.getItems().put(productId, quantity);
            cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Produto não encontrado no carrinho");
        }
    }
}