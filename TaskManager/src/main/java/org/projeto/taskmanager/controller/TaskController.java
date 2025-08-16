package org.projeto.taskmanager.controller;

import jakarta.validation.Valid;
import org.projeto.taskmanager.dto.TaskDTO;
import org.projeto.taskmanager.models.enums.TaskStatus;
import org.projeto.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TaskDTO>> getPendingTasks() {
        List<TaskDTO> tasks = taskService.findTasksByStatus(TaskStatus.PENDING);
        return ResponseEntity.ok(tasks);
    }

}
