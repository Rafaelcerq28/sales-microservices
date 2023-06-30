package com.br.inventorrycontrol.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.inventorrycontrol.productservice.model.InventoryMovement;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement,Long>{
    
}
