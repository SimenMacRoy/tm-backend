package com.example.tmbackend.dto;

import com.example.tmbackend.model.Group;
import com.example.tmbackend.model.User;

import java.util.Date;
import java.util.List;

public class TaskDTO {

    private Integer id;
    private String name;
    private String description;
    private Date dueDate;
    private String status;
    private Group group;
    private User admin;
    private List<User> assignedMembers;

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }

    public List<User> getAssignedMembers() { return assignedMembers; }
    public void setAssignedMembers(List<User> assignedMembers) { this.assignedMembers = assignedMembers; }
}
