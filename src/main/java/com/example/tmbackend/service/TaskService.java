package com.example.tmbackend.service;

import com.example.tmbackend.dto.TaskResponseDTO;
import com.example.tmbackend.exceptions.NotFoundException;
import com.example.tmbackend.model.Group;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.GroupRepository;
import com.example.tmbackend.repository.TaskRepository;
import com.example.tmbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, GroupRepository groupRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    public List<TaskResponseDTO> getAllTasksByAdmin(Integer adminId) {
        return taskRepository.findByAdminId(adminId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTasksByAssignedMember(Integer userId) {
        return taskRepository.findByAssignedMembers_Id(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TaskResponseDTO createTask(Task task, Integer groupId, Integer adminId, List<Integer> memberIds) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Groupe non trouvé"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Administrateur non trouvé"));

        task.setGroup(group);
        task.setAdmin(admin);

        if (memberIds != null && !memberIds.isEmpty()) {
            List<User> members = StreamSupport
                    .stream(userRepository.findAllById(memberIds).spliterator(), false)
                    .collect(Collectors.toList());
            task.setAssignedMembers(members);
        }

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id))
            throw new NotFoundException("Tâche introuvable pour suppression");
        taskRepository.deleteById(id);
    }

    public TaskResponseDTO updateTask(Integer id, Task updatedTask, Integer userId, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tâche introuvable"));

        if (isAdmin) {
            task.setName(updatedTask.getName());
            task.setDueDate(updatedTask.getDueDate());
            task.setStatus(updatedTask.getStatus());
            task.setDescription(updatedTask.getDescription());
            if (updatedTask.getAssignedMembers() != null) {
                task.setAssignedMembers(updatedTask.getAssignedMembers());
            }
        } else {
            if (task.getAssignedMembers().stream().noneMatch(m -> m.getId().equals(userId))) {
                throw new NotFoundException("Vous n'êtes pas autorisé à modifier cette tâche.");
            }
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());
        }

        return toDTO(taskRepository.save(task));
    }

    public TaskResponseDTO getTaskById(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tâche non trouvée"));
        return toDTO(task);
    }

    public Map<String, List<TaskResponseDTO>> getTasksGroupedByStatus(Integer adminId) {
        List<Task> tasks = taskRepository.findByAdminId(adminId);
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus,
                        Collectors.mapping(this::toDTO, Collectors.toList())));
    }

    private TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        dto.setGroupId(task.getGroup().getId());
        dto.setAdminId(task.getAdmin().getId());
        dto.setAssignedMemberIds(
                task.getAssignedMembers()
                        .stream().map(User::getId).collect(Collectors.toList())
        );
        return dto;
    }
}
