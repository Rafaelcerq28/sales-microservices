package com.br.inventorycontrol.productsales.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.inventorycontrol.productsales.model.Cart;
import com.br.inventorycontrol.productsales.model.Product;
import com.br.inventorycontrol.productsales.repository.CartRepository;

@Service
public class SalesService {
    CartRepository cartRepository;

    
    public SalesService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public ResponseEntity<Cart> addToCart(Long userId,Long productId,int quantity){   
        

        try {
            //map com a informacao a ser colocada no {id}
            HashMap<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id",productId);
            
            //ps*** essa funcao e so para validar se o produto existe
            //utilizo a funcao RestTemplate para acessar a outra api e armazeno o retorno em uma ResponseEntity
            ResponseEntity<Product> responseEntity = new RestTemplate().
            getForEntity("http://localhost:8000/product/{id}",
            Product.class, uriVariables);            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        Cart cartToSave = cartRepository.save(new Cart( userId, productId,quantity));

        //URI to save the product's location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
        path("/{id}").buildAndExpand(cartToSave.getId()).toUri();
        
        return ResponseEntity.created(location).body(cartToSave);
    }
    
    public ResponseEntity<Cart> deleteFromCart(Long userId, Long productId) {
        
        try {
            //map com a informacao a ser colocada no {id}
            HashMap<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id",productId);
            
            //ps*** essa funcao e so para validar se o produto existe
            //utilizo a funcao RestTemplate para acessar a outra api e armazeno o retorno em uma ResponseEntity
            ResponseEntity<Product> responseEntity = new RestTemplate().
            getForEntity("http://localhost:8000/product/{id}",
            Product.class, uriVariables);            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        
        cartRepository.DeleteByUserAndProductId(userId,productId);
        return ResponseEntity.noContent().build();
    }

    public List<Product> getAllFromCart(Long userId){
            /*
             * busco no banco todos os itens do usuario
             * faco um for pegando no outro servico cada um dos produtos e salvando em uma lista 
             * atualizar a quantidade para o valor que o cliente possui no carrinho
             */

        List<Cart> itensInCart = cartRepository.findByUserId(userId);
        
        ArrayList<Product> products = new ArrayList<>();
        
        for (Cart cart : itensInCart) {
            ResponseEntity<Product> responseEntity;
            try {
                //map com a informacao a ser colocada no {id}
                HashMap<String, Long> uriVariables = new HashMap<>();
                uriVariables.put("id",cart.getProductId());
                
                //ps*** essa funcao e so para validar se o produto existe
                //utilizo a funcao RestTemplate para acessar a outra api e armazeno o retorno em uma ResponseEntity
                responseEntity = new RestTemplate().
                getForEntity("http://localhost:8000/product/{id}",
                Product.class, uriVariables);     
            
            } catch (Exception e) {
                continue;
            }

            Product productToAdd = responseEntity.getBody();
            productToAdd.setQuantity(cart.getQuantity());
            products.add(productToAdd);
        }
        
        return products;
        //Fim do utilizar no futuro
    }



}
