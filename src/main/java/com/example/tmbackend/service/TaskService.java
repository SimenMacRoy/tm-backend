package com.example.tmbackend.service;

import com.example.tmbackend.dto.TaskCreateDTO;
import com.example.tmbackend.dto.TaskResponseDTO;
import com.example.tmbackend.dto.TaskUpdateDTO;
import com.example.tmbackend.exceptions.NotFoundException;
import com.example.tmbackend.model.Group;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.GroupRepository;
import com.example.tmbackend.repository.TaskRepository;
import com.example.tmbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

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

    public List<TaskResponseDTO> getAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        return StreamSupport
                .stream(tasks.spliterator(), false)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public TaskResponseDTO createTaskFromDTO(TaskCreateDTO dto) {
        System.out.println("DTO reçu : " + dto.getName() + ", " + dto.getGroupId() + ", " + dto.getAdminId());

        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new NotFoundException("Groupe introuvable"));

        User admin = userRepository.findById(dto.getAdminId())
                .orElseThrow(() -> new NotFoundException("Admin introuvable"));

        List<User> members = new ArrayList<>();
        if (dto.getAssignedMembers() != null && !dto.getAssignedMembers().isEmpty()) {
            members = StreamSupport
                    .stream(userRepository.findAllById(dto.getAssignedMembers()).spliterator(), false)
                    .collect(Collectors.toList());
            System.out.println("Membres assignés trouvés : " + members.size());
        }

        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        task.setGroup(group);
        task.setAdmin(admin);
        task.setAssignedMembers(members);

        Task saved = taskRepository.save(task);
        System.out.println("Tâche sauvegardée avec l'ID : " + saved.getId());

        return toDTO(saved);
    }


    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id))
            throw new NotFoundException("Tâche introuvable pour suppression");
        taskRepository.deleteById(id);
    }

    public TaskResponseDTO updateTask(Integer id, TaskUpdateDTO dto, Integer userId, boolean isAdmin) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tâche introuvable"));

        // Si c’est un administrateur, il peut tout modifier
        if (isAdmin) {
            task.setName(dto.getName());
            task.setDueDate(dto.getDueDate());
            task.setStatus(dto.getStatus());
            task.setDescription(dto.getDescription());

            if (dto.getAssignedMembers() != null && !dto.getAssignedMembers().isEmpty()) {
                List<User> assignedUsers = StreamSupport
                        .stream(userRepository.findAllById(dto.getAssignedMembers()).spliterator(), false)
                        .collect(Collectors.toList());

                task.setAssignedMembers(assignedUsers);
            }
        } else {
            // Si c’est un membre, il ne peut modifier que la description ou le statut
            boolean isAssigned = task.getAssignedMembers().stream()
                    .anyMatch(user -> user.getId().equals(userId));

            if (!isAssigned) {
                throw new NotFoundException("Vous n’êtes pas autorisé à modifier cette tâche.");
            }

            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
        }

        // Enregistre les modifications et retourne le DTO mis à jour
        Task updatedTask = taskRepository.save(task);
        return toDTO(updatedTask);
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

    public Map<String, List<TaskResponseDTO>> getTasksByAssignedMemberGroupedByStatus(Integer userId) {
        List<Task> tasks = taskRepository.findByAssignedMembers_Id(userId);
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        Task::getStatus,
                        Collectors.mapping(this::toDTO, Collectors.toList())
                ));
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
