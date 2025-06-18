// src/main/java/com/br/ecommerce/external/fakestore/FakeStoreService.java
package com.br.ecommerce.external.fakestore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.ecommerce.domain.product.Product;
import com.br.ecommerce.dto.FakeStoreProductDTO;

import java.util.List;
import java.util.Optional;

@Service
public class FakeStoreService {
    
    @Value("${fakestore.api.url}")
    private String apiUrl;
    
    private final RestTemplate restTemplate;
    private final FakeStoreProductAdapter adapter;
    
    public FakeStoreService(RestTemplate restTemplate, FakeStoreProductAdapter adapter) {
        this.restTemplate = restTemplate;
        this.adapter = adapter;
    }
    
    public List<Product> fetchAllProducts() {
        ResponseEntity<List<FakeStoreProductDTO>> response = restTemplate.exchange(
            apiUrl + "/products",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<FakeStoreProductDTO>>() {}
        );
        
        return response.getBody().stream()
            .map(adapter::adapt)
            .toList();
    }

 public List<Product> fetchPageableProducts(int limit, int offset) {
        // A FakeStoreAPI geralmente não suporta 'offset' diretamente, apenas 'limit'
        // Sendo assim, iremos buscar todos e depois aplicar a lógica de offset/limit em memória
        // ou, se a API suportar, construir a URL com os parâmetros de paginação.
        // Para a FakeStore, usaremos 'limit' para simular a quantidade e 'offset' para "pular"
        // os primeiros resultados, se a API suportar. Se não, precisaremos buscar tudo e paginar em memória.
        
        String url = UriComponentsBuilder.fromUriString(apiUrl + "/products")
            .queryParam("limit", limit)
            // .queryParam("offset", offset) // FakeStoreAPI não tem suporte nativo a offset.
            .toUriString();

        ResponseEntity<List<FakeStoreProductDTO>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<FakeStoreProductDTO>>() {}
        );
        
        List<Product> allProducts = response.getBody().stream()
            .map(adapter::adapt)
            .toList();

        // Como a FakeStoreAPI não tem 'offset' nativo, aplicamos a lógica de offset em memória
        if (offset < allProducts.size()) {
            return allProducts.subList(offset, Math.min(offset + limit, allProducts.size()));
        }
        return List.of(); // Retorna uma lista vazia se o offset for maior que o número de produtos
    }
    
    public Optional<Product> fetchProductById(Long id) {
        try {
            FakeStoreProductDTO dto = restTemplate.getForObject(
                apiUrl + "/products/" + id,
                FakeStoreProductDTO.class
            );
            return Optional.ofNullable(adapter.adapt(dto));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<String> fetchAllCategories() {
        ResponseEntity<List<String>> categoriesResponse = restTemplate.exchange(
            apiUrl + "/products/categories",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<String>>() {} 
            );
        return categoriesResponse.getBody().stream().toList();
    }

        public List<Product> fetchProductsByCategory(String category) {
        String url = UriComponentsBuilder.fromUriString(apiUrl + "/products/category/" + category)
            .toUriString();

        ResponseEntity<List<FakeStoreProductDTO>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<FakeStoreProductDTO>>() {}
        );

        return response.getBody().stream()
            .map(adapter::adapt)
            .toList();
    }
}