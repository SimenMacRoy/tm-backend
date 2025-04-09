package com.example.tmbackend.repository;

import com.example.tmbackend.model.Group;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Integer> {
    List<Task> findByAdminId(Integer adminId);
    List<Task> findByGroupId(Integer groupId);

    List<Task> findByAdmin(User admin);
}
