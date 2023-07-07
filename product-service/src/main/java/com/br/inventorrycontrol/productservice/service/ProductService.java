package com.br.inventorrycontrol.productservice.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.inventorrycontrol.productservice.controller.ProductController;
import com.br.inventorrycontrol.productservice.exception.ItemNotFoundException;
import com.br.inventorrycontrol.productservice.model.InventoryMovement;
import com.br.inventorrycontrol.productservice.model.MovementType;
import com.br.inventorrycontrol.productservice.model.Product;
import com.br.inventorrycontrol.productservice.repository.InventoryMovementRepository;
import com.br.inventorrycontrol.productservice.repository.ProductRepository;

@Service
public class ProductService {
    
    private ProductRepository productRepository;
    private InventoryMovementRepository inventoryMovementRepository;

    public ProductService(ProductRepository productRepository, InventoryMovementRepository inventoryMovementRepository) {
        this.productRepository = productRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
    }

    /*
     * Create a product with the first product's inventory movement and returnig the product's location
     */

    public ResponseEntity<Product> createProduct(Product product){
        
        Product savedProduct = productRepository.save(product);

        //generating product uri
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
        path("/{id}").buildAndExpand(savedProduct.getId()).toUri();

        //When the product is created the system add the first inventory movement
        MovementType movementType = MovementType.IN;
        InventoryMovement inventoryMovement = new InventoryMovement(LocalDateTime.now(),savedProduct.getQuantity(),movementType,"First Register",savedProduct);
        inventoryMovementRepository.save(inventoryMovement);

        return ResponseEntity.created(location).body(savedProduct);
    }

    /*
     * Get a list with all products
     */
    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    /*
     * Get one product by ID and return it with link to go back to all products and to go product's inventory movement
     */
    public EntityModel<Product> findProduct(Long id){
        Optional<Product> product = productRepository.findById(id);
        
        if(product.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }

        //Sending link to go back to all products
        EntityModel<Product> entityModel = EntityModel.of(product.get());
        WebMvcLinkBuilder link = linkTo(methodOn(ProductController.class).findAllProducts());
        entityModel.add(link.withRel("all-products"));
    
        WebMvcLinkBuilder link2 = linkTo(methodOn(ProductController.class).findAllInventoryMovements(id));
        entityModel.add(link2.withRel("inventory-movements"));

        return entityModel;
    }

    /*
     * Delete one product
     */
    public ResponseEntity<Object> deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        
        if(product.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }

        productRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * Update one product
     */
    public ResponseEntity<Product> updateProduct(Product product, Long id) {
        Optional<Product> productToUpdate = productRepository.findById(id);
        
        if(productToUpdate.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }

        productToUpdate.get().setName(product.getName());
        productToUpdate.get().setDescription(product.getDescription());
        productToUpdate.get().setBrand(product.getBrand());
        productToUpdate.get().setMaxQuantity(product.getMaxQuantity());
        productToUpdate.get().setMinQuantity(product.getMinQuantity());
        productToUpdate.get().setQuantity(product.getQuantity());
        productToUpdate.get().setWeight(product.getWeight());

        Product updatedProduct = productRepository.save(productToUpdate.get());

        /*
        {
        "name": "Product X",
        "description": "This is a fantastic product",
        "brand": "ABC Corporation",
        "maxQuantity": 100,
        "minQuantity": 10,
        "quantity": 50,
        "weight": 2.5
        }
        */
        
        return ResponseEntity.ok().body(updatedProduct);
    }

    /*
     * METHOD TO INCREASE A PRODUCT AMOUNT IN STOCK, THIS METHOD DOESN'T GIVE AN OPTION TO PUT A DESCRIPTION 
     */
    public ResponseEntity<Product> increaseProductStock(Long id, int quantity) {
        Optional<Product> productToUpdate = productRepository.findById(id);

        if(productToUpdate.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }
        
        productToUpdate.get().setQuantity(productToUpdate.get().getQuantity() + quantity);

        Product updatedProduct = productRepository.save(productToUpdate.get());
        
        MovementType movementType = MovementType.IN;
        //updating product's movement
        InventoryMovement inventoryMovement = new InventoryMovement(LocalDateTime.now(),quantity,movementType,updatedProduct);
        inventoryMovementRepository.save(inventoryMovement);

        return ResponseEntity.ok().body(updatedProduct);
    }

    /*
     * increase Product in Stock giving a description
     */
    public ResponseEntity<Product> increaseProductStock(Long id, InventoryMovement inventoryMovement) {
        Optional<Product> productToUpdate = productRepository.findById(id);

        if(productToUpdate.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }
        
        productToUpdate.get().setQuantity(productToUpdate.get().getQuantity() + inventoryMovement.getQuantity());

        Product updatedProduct = productRepository.save(productToUpdate.get());
        
        MovementType movementType = MovementType.IN;
        //updating product's movement
        InventoryMovement inventoryMovementToSave = new InventoryMovement(LocalDateTime.now(),inventoryMovement.getQuantity(),movementType,inventoryMovement.getDescription(),updatedProduct);
        inventoryMovementRepository.save(inventoryMovementToSave);

        return ResponseEntity.ok().body(updatedProduct);
    }
    
    /*
     * Decrease product amount in stock
     */
    public ResponseEntity<Product> decreaseProductStock(Long id, InventoryMovement inventoryMovement) {
        Optional<Product> productToUpdate = productRepository.findById(id);

        if(productToUpdate.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }
        
        productToUpdate.get().setQuantity(productToUpdate.get().getQuantity() - Math.abs(inventoryMovement.getQuantity()));

        Product updatedProduct = productRepository.save(productToUpdate.get());
        
        MovementType movementType = MovementType.OUT;
        //updating product's movement                                                           //Turns into negative
        InventoryMovement inventoryMovementToSave = new InventoryMovement(LocalDateTime.now(),(inventoryMovement.getQuantity() * -1),movementType,inventoryMovement.getDescription(),updatedProduct);
        inventoryMovementRepository.save(inventoryMovementToSave);

        return ResponseEntity.ok().body(updatedProduct);
    }

    /*
     * Finds all inventory's movement
     */
    public List<InventoryMovement> findAllInventoryMovements(Long id) {
        Optional<Product> product = productRepository.findById(id);
        
        if(product.isPresent() == false){
            throw new ItemNotFoundException("id "+id+" not found");
        }
        System.out.println(product.get().getInventoryMovements());
        return product.get().getInventoryMovements();
    }

    /*
     * Find especific procucts searching them by ID
     */
    public List<Product> findAllProductsByIds(List<Long> productIds) {
         
        return productRepository.findAllById(productIds);
    }

}
