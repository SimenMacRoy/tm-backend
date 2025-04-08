package com.example.tmbackend.controller;

import com.example.tmbackend.dto.UserDTO;
import com.example.tmbackend.model.User;
import com.example.tmbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task-master/users")
@CrossOrigin(origins = "http://localhost:4200")
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

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateMyProfile(@RequestParam Integer userId, @RequestBody User updatedUser) {
        if (!userId.equals(updatedUser.getId())) {
            throw new IllegalArgumentException("L'ID fourni ne correspond pas à l'utilisateur.");
        }
        User updated = userService.updateUser(updatedUser);
        return ResponseEntity.ok(userService.mapToDTO(updated));
    }


    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody User user){
        User insertedUser = this.userService.insertUser(user);
        return ResponseEntity.ok(userService.mapToDTO(insertedUser));
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable int id, @RequestBody User user) {
        if (id != user.getId()) {
            throw new IllegalArgumentException("L'ID spécifié dans l'URL ne correspond pas à celui du membre fourni.");
        }
        this.userService.updateUser(user);
        return userService.mapToDTO(user);
    }


    @PostMapping("/login")
    public UserDTO login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        return userService.loginAndGetDTO(email, password);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(@RequestParam Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Compte supprimé avec succès.");
    }



}
