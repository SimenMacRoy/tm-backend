package com.example.tmbackend.repository;

import com.example.tmbackend.model.Group;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByAdminId(Integer adminId);
    List<Task> findByAssignedMembers_Id(Integer userId);
    List<Task> findByAdminIdAndStatus(Integer adminId, String status);
}

