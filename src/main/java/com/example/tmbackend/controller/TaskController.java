package com.example.tmbackend.controller;

import com.example.tmbackend.dto.TaskCreateDTO;
import com.example.tmbackend.dto.TaskResponseDTO;
import com.example.tmbackend.dto.TaskUpdateDTO;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task-master/tasks")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "Authorization")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDTO> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        List<TaskResponseDTO> taskList = new ArrayList<>(tasks);
        return taskList.stream().map(task -> taskService.getTaskById(task.getId())).collect(Collectors.toList());
    }

    @GetMapping("/admin/{adminId}")
    public List<TaskResponseDTO> getTasksByAdmin(@PathVariable Integer adminId) {
        return taskService.getAllTasksByAdmin(adminId);
    }

    @GetMapping("/assigned/{memberId}")
    public List<TaskResponseDTO> getTasksAssignedToMember(@PathVariable Integer memberId) {
        return taskService.getTasksByAssignedMember(memberId);
    }

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody TaskCreateDTO dto) {
        return taskService.createTaskFromDTO(dto);
    }


    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable Integer id,
                                      @RequestBody TaskUpdateDTO dto,
                                      @RequestParam Integer userId,
                                      @RequestParam boolean isAdmin) {
        return taskService.updateTask(id, dto, userId, isAdmin);
    }



    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Integer id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/status-summary")
    public Map<String, List<TaskResponseDTO>> getStatusSummary(@RequestParam Integer adminId) {
        return taskService.getTasksGroupedByStatus(adminId);
    }

    @GetMapping("/assigned/{memberId}/grouped")
    public Map<String, List<TaskResponseDTO>> getTasksGroupedByStatusForMember(@PathVariable Integer memberId) {
        return taskService.getTasksByAssignedMemberGroupedByStatus(memberId);
    }

}