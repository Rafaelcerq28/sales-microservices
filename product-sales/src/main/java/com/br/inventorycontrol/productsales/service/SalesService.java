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
    }

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
     * criar metodo finish and pay - DONE
     * CRIAR UMA CLASSE INVENTORY MOVEMENT - DONE
     * colocar esse metodo para baixar o estoque na api ProductService - DONE
     * Puxar os itens do carrinho
     * Criar um for para ir baixando item por item do carrinho 
     * limpar o carrinho do usuario
     * configurar o metodo do checkout para informar se o produto esta indisponivel
     */

    public String finishAndPay(Long userId){

        Long productId = (long) 1001;

        ResponseEntity<InventoryMovement> responseEntity;
        try {
            HttpHeaders headers = new HttpHeaders();                  
            headers.setContentType(MediaType.APPLICATION_JSON);//headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            //map com a informacao a ser colocada no {id}
            HashMap<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id",productId);

            InventoryMovement inventoryMovement = new InventoryMovement(10,"teste");
            HttpEntity<InventoryMovement> requestUpdate = new HttpEntity<>(inventoryMovement, headers);
            //ps*** essa funcao e so para validar se o produto existe
            //utilizo a funcao RestTemplate para acessar a outra api e armazeno o retorno em uma ResponseEntity
                responseEntity = new RestTemplate().
                exchange("http://localhost:8000/product/{id}/decrease-stock",
                HttpMethod.PUT,
                requestUpdate,
                InventoryMovement.class, uriVariables);           
        } catch (Exception e) {
            responseEntity = ResponseEntity.notFound().build();
        }

        /*
        * InventoryMovement inventoryMovement = new InventoryMovement();

        String resourceUrl = 
        URI + '/' + createResponse.getBody().getId();
        HttpEntity<Foo> requestUpdate = new HttpEntity<>(inventoryMovement, headers);
        RestTemplate.exchange(resourceUrl, HttpMethod.PUT, requestUpdate, Void.class);
        */


        return "passou";
    }
}
