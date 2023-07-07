package com.br.inventorycontrol.productsales.model;

import java.util.List;

public class Checkout {

    public Long userId;//receber um objeto usuario no futuro
    public List<Product> products;
    public double total;

    public Checkout(Long userId, List<Product> products, double total) {
        this.userId = userId;
        this.products = products;
        this.total = total;
    }

    public Checkout() {
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }

}
