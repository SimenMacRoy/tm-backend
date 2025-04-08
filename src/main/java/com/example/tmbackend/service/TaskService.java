package com.example.tmbackend.service;

import com.example.tmbackend.dto.TaskDTO;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.TaskRepository;
import com.example.tmbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // Créer une nouvelle tâche
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Récupérer toutes les tâches d'un admin
    public List<Task> getTasksByAdmin(User admin) {
        return taskRepository.findByAdmin(admin);
    }

    // Mettre à jour une tâche
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    // Supprimer une tâche
    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    // Mapper Task à TaskDTO
    public TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        dto.setGroup(task.getGroup());
        dto.setAdmin(task.getAdmin());
        dto.setAssignedMembers(task.getAssignedMembers());
        return dto;
    }

    // Récupérer toutes les tâches d'un groupe
    public List<Task> getTasksByGroup(Integer groupId) {
        return taskRepository.findByGroupId(groupId);
    }
}
