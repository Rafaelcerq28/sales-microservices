package com.br.inventorrycontrol.productservice.controller;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.br.inventorrycontrol.productservice.model.InventoryMovement;
import com.br.inventorrycontrol.productservice.model.Product;
import com.br.inventorrycontrol.productservice.service.ProductService;

@RestController
public class ProductController {
    
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    //save
    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        return productService.createProduct(product);
    }
    //get all
    @GetMapping("/product")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAllProducts(){
        return productService.findAllProducts();
    }

    //get all by Ids
    @GetMapping("/product/list")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAllProductsByIds(@RequestBody List<Long> productIds){
        return productService.findAllProductsByIds(productIds);
    }

    //get one by Id
    @GetMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Product> findProduct(@PathVariable(value = "id") Long id){
        return productService.findProduct(id);
    }

    //delete one
    @DeleteMapping("/product/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") Long id){
        return productService.deleteProduct(id);
    }

    //update one
    @PutMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long id, @RequestBody Product product){
        return productService.updateProduct(product,id);
    }

    //increasing inventory giving a description
    @PutMapping("/product/{id}/increase-stock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> increaseProductStock(@PathVariable(value = "id") Long id,@RequestBody InventoryMovement inventoryMovement){
        return productService.increaseProductStock(id,inventoryMovement);
    }
    //increasing inventory
    @PutMapping("/product/{id}/increase-stock/{quantity}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> increaseProductStock(@PathVariable(value = "id") Long id, @PathVariable(value = "quantity") int quantity){
        return productService.increaseProductStock(id,quantity);
    }

    //Decreasing inventory
    @PutMapping("/product/{id}/decrease-stock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Product> decreaseProductStock(@PathVariable(value = "id") Long id,@RequestBody InventoryMovement inventoryMovement){
        return productService.decreaseProductStock(id,inventoryMovement);
    }

    //Get a list with updates from the product
    @GetMapping("/product/{id}/inventory-movement")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryMovement> findAllInventoryMovements (@PathVariable(value = "id") Long id){
        return productService.findAllInventoryMovements(id);
    }
}
