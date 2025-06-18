package com.br.ecommerce.service;

import com.br.ecommerce.domain.Customer;
import com.br.ecommerce.repository.CustomerRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService extends DefaultOAuth2UserService {
    
    private final CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Long githubId = Long.valueOf(oauth2User.getAttribute("id").toString());
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email não fornecido pelo provedor OAuth2. O login não pode ser concluído.");
        }

        Optional<Customer> existingCustomer = customerRepository.findById(githubId);
        Customer customer;

        if (existingCustomer.isPresent()) {
            customer = existingCustomer.get();
            // Opcional: Atualizar os dados do cliente se eles puderem mudar no GitHub
            // customer.setEmail(email);
            // customer.setName(name);
            // customerRepository.save(customer);
        } else {
            customer = new Customer();
            customer.setId(githubId);
            customer.setEmail(email);
            customer.setName(name);

            customer = customerRepository.save(customer);
        }
        return customer; 
    }
}

// // src/main/java/com/br/ecommerce/service/AuthService.java
// package com.br.ecommerce.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.stereotype.Service;
// import com.br.ecommerce.domain.Customer;
// import com.br.ecommerce.repository.CustomerRepository;

// @Service
// public class AuthService {
    
//     @Autowired
//     private CustomerRepository customerRepository;

//     public Customer loginViaGitHub(OAuth2User oauth2User) {
//         Long githubId = Long.parseLong(oauth2User.getAttribute("id").toString());
//         String email = oauth2User.getAttribute("email");
//         String name = oauth2User.getAttribute("name");

//         return customerRepository.findById(githubId)
//             .orElseGet(() -> {
//                 Customer newCustomer = new Customer();
//                 newCustomer.setId(githubId);
//                 newCustomer.setEmail(email);
//                 newCustomer.setName(name);
//                 return customerRepository.save(newCustomer);
//             });
//     }
// }