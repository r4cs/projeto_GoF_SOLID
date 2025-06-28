package com.br.ecommerce.service;

import com.br.ecommerce.domain.*;
import com.br.ecommerce.dto.FakeStoreProductDTO;
import com.br.ecommerce.dto.OrderItemDTO;
import com.br.ecommerce.dto.OrderRequestDTO;
import com.br.ecommerce.external.fakestore.FakeStoreProductAdapter;
import com.br.ecommerce.repository.OrderRepository;
import com.br.ecommerce.repository.ProductRepository;
import com.br.ecommerce.service.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductFacade productFacade;
    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final FakeStoreProductAdapter adapter;
    
    public OrderService(OrderRepository orderRepository,
                      ProductFacade productFacade,
                      NotificationService notificationService,
                      ProductRepository productRepository,
                      FakeStoreProductAdapter adapter) {
        this.orderRepository = orderRepository;
        this.productFacade = productFacade;
        this.notificationService = notificationService;
        this.productRepository = productRepository;
        this.adapter = adapter;
    }
    
    public Order createOrder(OrderRequestDTO request) {
        Order order = new Order();
        order.setCustomer(request.getCustomer());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        
        request.getItems().forEach(item -> {
            Product product = productFacade.getProductById(item.getProductId())
                .orElseThrow(() -> new OrderException("Produto não encontrado: " + item.getProductId()));

            FakeStoreProductDTO productDTO = adapter.adaptToDto(product);

            if (product.getId() != null && productRepository.existsById(product.getId())) {
                // product = productRepository.save(product);
                product = productFacade.createProduct(productDTO);
            }
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setOrder(order);
            
            order.addItem(orderItem);
        });

        // !!!
        Order savedOrder = orderRepository.save(order); 

        System.out.println("\nSavedOrder em order Service: " + savedOrder.toString());
        System.out.println("SavedOrder get id em order Service: " + savedOrder.getId());
        System.out.println("SavedOrder get total priceem order Service: " + savedOrder.getTotalPrice());

        // Padrão Observer via NotificationService (Decorator)
        notificationService.send("\nPedido criado: #" + savedOrder.getId());
        
        return savedOrder;
    }

    public Order createOrderFromCart(Customer customer) {
    OrderRequestDTO request = new OrderRequestDTO();
    request.setCustomer(customer);
    
    // Obter os itens do carrinho já como OrderItemDTO com produtos completos
    List<OrderItemDTO> items = customer.getCart().getItems().entrySet().stream()
        .map(entry -> {
            Product product = productFacade.getProductById(entry.getKey())
                .orElseThrow(() -> new OrderException("Produto não encontrado: " + entry.getKey()));
            return new OrderItemDTO(product.getId(), entry.getValue(), entry.getKey());
        })
        .collect(Collectors.toList());
    
    request.setItems(items);
    System.out.println("\nrequest items em order service: " + request.getItems());
    System.out.println("request customer id em order service: " + request.getCustomer().getId());
    return createOrder(request);
}
    
    @Transactional
    public void advanceOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderException("Pedido não encontrado: " + orderId));
        
        // Padrão State para transição de status
        if (!order.getStatus().canAdvance()) {
            throw new IllegalStateException("Não é possível avançar o status atual");
        }
        
        OrderStatus newStatus = order.getStatus().next();
        order.setStatus(newStatus);
        orderRepository.save(order);
        
        // Notificação com padrão Decorator
        notificationService.send("Status do pedido #" + orderId + " atualizado para: " + newStatus);
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    // Classe de exceção específica do domínio
    public static class OrderException extends RuntimeException {
        public OrderException(String message) {
            super(message);
        }
    }
}