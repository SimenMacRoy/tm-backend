package com.example.tmbackend.service;

import com.example.tmbackend.dto.UserDTO;
import com.example.tmbackend.exceptions.InvalidDataException;
import com.example.tmbackend.exceptions.NotFoundException;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository ;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        this.userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUserById(Integer id) throws NotFoundException {
        return this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Aucun membre n'a été trouvé avec l'ID " + id + ".")
        );
    }

    public User insertUser(User user) throws InvalidDataException{
        if(user.getId() != null){
            throw new InvalidDataException("L'ID du membre doit être vide afin d'être déterminé via la séquence.");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setTelephone(user.getTelephone());
            updatedUser.setAddress(user.getAddress());
            updatedUser.setPassword(encodedPassword);
            updatedUser.setRole(user.getRole());

            if(user.getPassword() != null && !user.getPassword().isEmpty()){
                String encryptedPassword = passwordEncoder.encode(user.getPassword());
                updatedUser.setPassword(encryptedPassword);
            } else {
                updatedUser.setPassword(existingUser.get().getPassword());
            }
            return userRepository.save(updatedUser);
        } else {
            throw new NotFoundException("L'utilisateur avec l'ID " + user.getId() + " n'existe pas.");
        }
    }


    public UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getTelephone(),
                user.getAddress(),
                user.getRole()
        );
    }

    public List<UserDTO> getAllUsersDTO() {
        List<UserDTO> dtos = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> dtos.add(mapToDTO(user)));
        return dtos;
    }

    public UserDTO getUserDTOById(Integer id) {
        return mapToDTO(getUserById(id));
    }

}
