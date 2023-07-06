package com.br.inventorycontrol.productsales.service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.inventorycontrol.productsales.model.Cart;
import com.br.inventorycontrol.productsales.model.Checkout;
import com.br.inventorycontrol.productsales.model.InventoryMovement;
import com.br.inventorycontrol.productsales.model.Product;
import com.br.inventorycontrol.productsales.repository.CartRepository;


/*
 * TO DO
 * Criar exceptions com mensagens de erro corretas, fazer validacao dos inputs itens nas models
 * ler mais sobre Logger e inserir nos metodos (Nas duas APIs)
 * inserir comentario nos metodos e limpar comentarios desnecessarios
 * VER VIDEOS SOBRE DTO E JAVA RECORDS
 */

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
            getForEntity("http://localhost:8000/products/{id}",
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
            getForEntity("http://localhost:8000/products/{id}",
            Product.class, uriVariables);            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        
        cartRepository.DeleteByUserAndProductId(userId,productId);
        return ResponseEntity.noContent().build();
    }

    public List<Product> getAllFromCart(Long userId){

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
                getForEntity("http://localhost:8000/products/{id}",
                Product.class, uriVariables);     
            
            } catch (Exception e) {
                continue;
            }

            Product productToAdd = responseEntity.getBody();
            productToAdd.setQuantity(cart.getQuantity());
            products.add(productToAdd);
        }
        
        return products;
    }

    //Criar verificacao para informar que o carrinho esta vazio
    public ResponseEntity<Checkout> checkout(Long userId){
        List<Product> products = getAllFromCart(userId);
        double total = 0;

        for (Product product : products) {
            total += (product.getPrice() * product.getQuantity());
        }
        
        Checkout checkout = new Checkout(userId, products, total);

        return ResponseEntity.ok().body(checkout);
    }

    /*
     * configurar o metodo do checkout para informar se o produto esta indisponivel
     */

    public String finishAndPay(Long userId){

        List<Product> products = getAllFromCart(userId);
      
        for(Product product : products){

            ResponseEntity<InventoryMovement> responseEntity;
            
            try {
                HttpHeaders headers = new HttpHeaders();                  
                headers.setContentType(MediaType.APPLICATION_JSON);
                //map com a informacao a ser colocada no {id}
                HashMap<String, Long> uriVariables = new HashMap<>();
                uriVariables.put("id",product.getId());

                String description = "Item sold at " + LocalDateTime.now() + " to user " + userId;
                InventoryMovement inventoryMovement = new InventoryMovement(product.getQuantity(),description);
                HttpEntity<InventoryMovement> requestUpdate = new HttpEntity<>(inventoryMovement, headers);
                String uri = "http://localhost:8000/products/{id}/decrease-stock";
                
                responseEntity = new RestTemplate().
                exchange(uri,HttpMethod.PUT,requestUpdate,InventoryMovement.class,uriVariables);        

                cartRepository.DeleteByUserAndProductId(userId, product.getId());

            } catch (Exception e) {
                return "Failure at product " + product.getName() + " " + product.getName();
            }

        }

        return "OK";
    }
}
