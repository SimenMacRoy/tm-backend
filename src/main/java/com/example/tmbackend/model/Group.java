package com.example.tmbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    private Integer id;

    private String name;

    private Date dateCreation;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members;

    @OneToMany(mappedBy = "group")
    private List<Task> tasks;

    // Getters et setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) { this.members = members; }

    public List<Task> getTasks() { return tasks; }
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}

