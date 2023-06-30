package com.br.inventorycontrol.productsales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.inventorycontrol.productsales.model.Cart;

import jakarta.transaction.Transactional;

//this annotation is necessary to make the delete itens on cart
@Transactional
@Repository
public interface CartRepository extends JpaRepository<Cart,Long>{

    //Query para fazer um select no banco para retornar apenas o que eu preciso.
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM CART WHERE USER_ID = :user_id AND PRODUCT_ID = :product_id")
    void DeleteByUserAndProductId(@Param("user_id") Long userId,@Param("product_id") Long productId);
    
    List<Cart> findByUserId(Long userId);
}
