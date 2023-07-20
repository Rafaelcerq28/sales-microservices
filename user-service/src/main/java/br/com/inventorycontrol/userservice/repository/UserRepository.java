package br.com.inventorycontrol.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.inventorycontrol.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
}
