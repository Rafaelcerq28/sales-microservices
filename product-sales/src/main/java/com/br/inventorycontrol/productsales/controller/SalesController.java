package com.br.inventorycontrol.productsales.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.inventorycontrol.productsales.model.Cart;
import com.br.inventorycontrol.productsales.model.Checkout;
import com.br.inventorycontrol.productsales.model.Product;
import com.br.inventorycontrol.productsales.service.SalesService;

@RestController
public class SalesController {
    
    
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    SalesService salesService;

    //revisar essa url para: /add-to-cart/user/{user_id}/produto/{product_id}/quantidade/{quantity} ?
    @PostMapping("/cart/{user_id}/{product_id}/{quantity}")
    public ResponseEntity<Cart> addToCart(
        @PathVariable(value = "user_id")Long userId, 
        @PathVariable(value="product_id") Long productId,
        @PathVariable(value = "quantity") int quantity){
        
        return salesService.addToCart(userId,productId,quantity);
    }

    @GetMapping("/cart/{user_id}")
    public List<Product> getAllFromCart(@PathVariable(value="user_id") Long userId){
        return salesService.getAllFromCart(userId);
    }

    @DeleteMapping("/cart/{user_id}/{product_id}")
    public ResponseEntity<Cart> deleteFromCart(
        @PathVariable(value = "user_id") Long userId,
        @PathVariable (value = "product_id") Long productId){

        return salesService.deleteFromCart(userId,productId);
    }

    @GetMapping("/cart/{user_id}/checkout")
    public ResponseEntity<Checkout> checkout(@PathVariable(value="user_id") Long userId){
        return salesService.checkout(userId);
    }

    @GetMapping("/cart/{user_id}/payment")
    public void finishAndPay(@PathVariable(value = "user_id") Long userId){
        salesService.finishAndPay(userId);
    }
}
