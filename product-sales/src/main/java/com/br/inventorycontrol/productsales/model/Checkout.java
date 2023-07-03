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


    /*{
    "id_usuario": 12345,
    "itens": [
        {
        "id": 1,
        "nome": "Camiseta",
        "preco": 29.99,
        "quantidade": 2,
        "categoria": "Roupas",
        "cor": "Azul",
        "tamanho": "M"
        },
        {
        "id": 2,
        "nome": "Calça Jeans",
        "preco": 59.99,
        "quantidade": 1,
        "categoria": "Roupas",
        "cor": "Preto",
        "tamanho": "L"
        },
        {
        "id": 3,
        "nome": "Tênis esportivo",
        "preco": 79.99,
        "quantidade": 1,
        "categoria": "Calçados",
        "cor": "Branco",
        "tamanho": "42"
        }
    ],
    "subtotal": 199.96,
    "desconto": 20.00,
    "valor_total": 179.96,
    "data_compra": "2023-07-01",
    "endereco_entrega": {
        "rua": "Avenida das Flores",
        "numero": "123",
        "bairro": "Centro",
        "cidade": "São Paulo",
        "estado": "SP",
        "cep": "01234-567"
    },
    "forma_pagamento": {
        "tipo": "Cartão de Crédito",
        "numero_cartao": "**** **** **** 5678",
        "nome_titular": "Fulano da Silva",
        "data_validade": "08/25",
        "codigo_seguranca": "123"
    }
    }
    */
}
