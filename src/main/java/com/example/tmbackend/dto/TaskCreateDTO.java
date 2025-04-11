package com.example.tmbackend.dto;

import java.util.Date;
import java.util.List;

public class TaskCreateDTO {
    private String name;
    private String description;
    private Date dueDate;
    private String status;
    private Integer groupId;
    private Integer adminId;
    private List<Integer> assignedMembers;

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public List<Integer> getAssignedMembers() {
        return assignedMembers;
    }

    public void setAssignedMembers(List<Integer> assignedMembers) {
        this.assignedMembers = assignedMembers;
    }
}
