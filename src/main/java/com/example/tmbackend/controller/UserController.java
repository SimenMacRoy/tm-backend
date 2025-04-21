package com.example.tmbackend.controller;

import com.example.tmbackend.dto.UserDTO;
import com.example.tmbackend.model.User;
import com.example.tmbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task-master/users")
@CrossOrigin(origins = "https://taskmaster-c6e9b.web.app", allowedHeaders = "*", exposedHeaders = "Authorization")
public class UserController {

    private final UserService userService ;

    public UserController(UserService userService){
        this.userService = userService ;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getAllUsersDTO();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Integer id) {
        return userService.getUserDTOById(id);
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User user){
        User insertedUser = this.userService.insertUser(user);
        return ResponseEntity.ok(userService.mapToDTO(insertedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody User user) {
        if (id != user.getId()) {
            throw new IllegalArgumentException("L'ID spécifié dans l'URL ne correspond pas à celui du membre fourni.");
        }
        try {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(userService.mapToDTO(updatedUser));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

}
