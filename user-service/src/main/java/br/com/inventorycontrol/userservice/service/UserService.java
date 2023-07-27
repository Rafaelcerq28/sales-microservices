package br.com.inventorycontrol.userservice.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.inventorycontrol.userservice.model.User;
import br.com.inventorycontrol.userservice.repository.UserRepository;
import jakarta.validation.Valid;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<User> saveUser(User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                        path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

    public ResponseEntity<User> findUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent() == false){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user.get());
    }
    


}
