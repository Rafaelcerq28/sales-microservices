package com.br.inventorycontrol.productsales.model;

public class InventoryMovement {

    private int quantity; 
    private String description;
    
    
    public InventoryMovement() {
    }

    public InventoryMovement(int quantity, String description) {
        this.quantity = quantity;
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    
}
