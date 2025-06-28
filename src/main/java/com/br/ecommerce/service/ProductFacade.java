// src/main/java/com/br/ecommerce/service/ProductFacade.java
package com.br.ecommerce.service;

import com.br.ecommerce.domain.Product;
import com.br.ecommerce.dto.FakeStoreProductDTO;
import com.br.ecommerce.external.fakestore.FakeStoreProductAdapter;
import com.br.ecommerce.external.fakestore.FakeStoreService;
import com.br.ecommerce.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductFacade {
    
    private final FakeStoreService fakeStoreService;
    private final ProductRepository productRepository;
    private final FakeStoreProductAdapter adapter;
    
    public ProductFacade(FakeStoreService fakeStoreService, 
                        ProductRepository productRepository,
                        FakeStoreProductAdapter adapter) {
        this.fakeStoreService = fakeStoreService;
        this.productRepository = productRepository;
        this.adapter = adapter;
    }
    
    public List<Product> getAllProducts() {
        // Primeiro tenta buscar da API externa
        List<Product> externalProducts = fakeStoreService.fetchAllProducts();
        
        if(externalProducts.isEmpty()) {
            // Fallback para o banco de dados local
            return productRepository.findAll();
        }
        
        return externalProducts;
    }

    public Optional<Product> getProductById(Long id) {
    Optional<Product> externalProduct = fakeStoreService.fetchProductById(id);
    
    if(externalProduct.isPresent()) {
        // Persiste o produto da API externa
        return Optional.of(productRepository.save(externalProduct.get()));
    }
    
    return productRepository.findById(id);
    }

    /**
     * Cria um novo produto, aplicando truncamento à descrição se exceder 255 caracteres.
     * @param FakeStoreProductDTO O DTO contendo os dados do produto.
     * @return O produto persistido.
     */
    public Product createProduct(FakeStoreProductDTO productDTO) {
            Product product = adapter.adaptToProduct(productDTO);

        String description = productDTO.getDescription();
        if (description != null && description.length() > 255) {
            product.setDescription(description.substring(0, 240).concat("(...)"));
        } else {
            product.setDescription(description);
        }

        return productRepository.save(product);
    }
    
    public Map<String, List<Product>> getProductsGroupedByCategory() {
        List<Product> products = getAllProducts();
        return products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));
    }
    
    public List<String> getAllCategories() {
        return fakeStoreService.fetchAllCategories();
    }

    public Page<Product> getProducts(Pageable pageable, String category) {
        List<Product> products;
        
        if (category != null && !category.isEmpty()) {
            // Se uma categoria é especificada, tenta buscar da API externa por categoria
            products = fakeStoreService.fetchProductsByCategory(category);
            if (products.isEmpty()) {
                // Fallback para o repositório local se a API externa não retornar produtos para a categoria
                products = productRepository.findByCategory(category);
            }
        } else {
            // Caso contrário, busca todos os produtos (com paginação)
            products = fakeStoreService.fetchPageableProducts(pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());
            if (products.isEmpty()) {
                // Fallback para o repositório local
                return productRepository.findAll(pageable);
            }
        }

        // Simula a paginação em memória para os resultados da FakeStoreAPI, se aplicável
        // A FakeStoreAPI não oferece paginação nativa por padrão, então simulamos aqui.
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), products.size());

        if (start > products.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, products.size());
        }
        
        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }
    
    // TODO: Outros métodos da fachada...
}