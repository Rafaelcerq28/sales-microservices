package br.com.inventorycontrol.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.inventorycontrol.userservice.model.User;
import br.com.inventorycontrol.userservice.service.UserService;
import jakarta.validation.Valid;

@RestController
public class UserController {
    
    private UserService userService;
    

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user){
        return userService.saveUser(user);
    }

}
