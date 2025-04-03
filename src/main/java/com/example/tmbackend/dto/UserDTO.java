package com.example.tmbackend.dto;

public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String address;
    private String role;

    // Constructeurs
    public UserDTO() {}

    public UserDTO(Integer id, String firstName, String lastName, String email, String telephone, String address, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.role = role;
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
