package com.example.tmbackend.controller;

import com.example.tmbackend.dto.TaskResponseDTO;
import com.example.tmbackend.model.Task;
import com.example.tmbackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task-master/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDTO> getTasksByAdmin(@RequestParam Integer adminId) {
        return taskService.getAllTasksByAdmin(adminId);
    }

    @GetMapping("/assigned")
    public List<TaskResponseDTO> getTasksByMember(@RequestParam Integer userId) {
        return taskService.getTasksByAssignedMember(userId);
    }

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody Task task,
                                      @RequestParam Integer groupId,
                                      @RequestParam Integer adminId,
                                      @RequestParam(required = false) List<Integer> memberIds) {
        return taskService.createTask(task, groupId, adminId, memberIds);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(@PathVariable Integer id,
                                      @RequestBody Task updatedTask,
                                      @RequestParam Integer userId,
                                      @RequestParam boolean isAdmin) {
        return taskService.updateTask(id, updatedTask, userId, isAdmin);
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
}