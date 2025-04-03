package com.example.tmbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users", schema="taskmaster_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String address;
    private String password;
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private List<Group> adminGroups;

    @OneToMany(mappedBy = "admin")
    private List<Task> adminTasks;

    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    private List<Group> groups;

    @ManyToMany(mappedBy = "assignedMembers")
    private List<Task> assignedTasks;

    public User(){}

    // Getters et setters

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Group> getAdminGroups() { return adminGroups; }
    public void setAdminGroups(List<Group> adminGroups) { this.adminGroups = adminGroups; }

    public List<Task> getAdminTasks() { return adminTasks; }
    public void setAdminTasks(List<Task> adminTasks) { this.adminTasks = adminTasks; }

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }

    public List<Task> getAssignedTasks() { return assignedTasks; }
    public void setAssignedTasks(List<Task> assignedTasks) { this.assignedTasks = assignedTasks; }
}

