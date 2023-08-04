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

import com.br.inventorycontrol.productsales.exception.ItemNotFoundException;
import com.br.inventorycontrol.productsales.model.Cart;
import com.br.inventorycontrol.productsales.model.Checkout;
import com.br.inventorycontrol.productsales.model.InventoryMovement;
import com.br.inventorycontrol.productsales.model.Product;
import com.br.inventorycontrol.productsales.model.User;
import com.br.inventorycontrol.productsales.repository.CartRepository;

@Service
public class SalesService {

    CartRepository cartRepository;
    
    public SalesService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    /*
     * Mehtod to insert intens in cart
     */
    public ResponseEntity<Cart> addToCart(Long userId,Long productId,int quantity){   
        
        try {
  
            /* Checking if this product exist in the product's API */
            HashMap<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id",productId);
            
            ResponseEntity<Product> responseEntity = new RestTemplate().
            getForEntity("http://localhost:8000/products/{id}",
            Product.class, uriVariables);            
        } catch (Exception e) {
            throw new ItemNotFoundException("Item "+ productId + " is not found");
        }

        try {
  
            /* Checking if this user exist in the user's API */
            HashMap<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id",userId);
            
            ResponseEntity<User> responseEntity = new RestTemplate().
            getForEntity("http://localhost:8765/users/{id}",
            User.class, uriVariables);            
        } catch (Exception e) {
            throw new ItemNotFoundException("User "+ userId + " is not found");
        }        


        Cart cartToSave = cartRepository.save(new Cart(userId, productId,quantity));

        /* URI to save the product's location */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
        path("/{id}").buildAndExpand(cartToSave.getId()).toUri();
        
        return ResponseEntity.created(location).body(cartToSave);
    }
    
    /*
     * Method to delete itens from cart
     */
    public ResponseEntity<Cart> deleteFromCart(Long userId, Long productId) {
         
        Optional<Cart> cartToDelete = cartRepository.findById(productId);

        if(cartToDelete.isPresent() == false){
            throw new ItemNotFoundException("Product" + productId + " not found in the cart");
        }
        
        try {
            /* Checking if this product exist in the product's API */
            HashMap<String, Long> uriVariables = new HashMap<>();
            uriVariables.put("id",productId);
        
            ResponseEntity<Product> responseEntity = new RestTemplate().
            getForEntity("http://localhost:8000/products/{id}",
            Product.class, uriVariables);            
        } catch (Exception ex) {
            throw new ItemNotFoundException("Item "+ productId + " is not found");
        }
        
        cartRepository.DeleteByUserAndProductId(userId,productId);
        return ResponseEntity.noContent().build();
    }

    /*
     * Get all itens from user's cart
     */
    public List<Product> getAllFromCart(Long userId){

        List<Cart> itensInCart = cartRepository.findByUserId(userId);
        
        if(itensInCart.isEmpty() == true){
            throw new ItemNotFoundException("User has no itens in the cart");
        }

        ArrayList<Product> products = new ArrayList<>();
        
        for (Cart cart : itensInCart) {
            ResponseEntity<Product> responseEntity;
            try {
                /* Checking if this product exist in the product's API */
                HashMap<String, Long> uriVariables = new HashMap<>();
                uriVariables.put("id",cart.getProductId());
                
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

    /*
     * Method to create a checkout list
     */
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
     * Methodo to finish the selling and update the product's stock value
     */
    public String finishAndPay(Long userId){

        List<Product> products = getAllFromCart(userId);
      
        for(Product product : products){

            ResponseEntity<InventoryMovement> responseEntity;
            
            try {
                /*
                 * This code is to update the product in the product's API
                 */
                HttpHeaders headers = new HttpHeaders();                  
                headers.setContentType(MediaType.APPLICATION_JSON);
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
