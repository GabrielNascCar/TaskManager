package org.projeto.taskmanager.service;

import jakarta.transaction.Transactional;
import org.projeto.taskmanager.dto.TaskDTO;
import org.projeto.taskmanager.models.Task;
import org.projeto.taskmanager.models.User;
import org.projeto.taskmanager.models.enums.TaskStatus;
import org.projeto.taskmanager.repository.TaskRepository;
import org.projeto.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {

        if (taskDTO.getDueDate() != null && taskDTO.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        Task task = convertToEntity(taskDTO);

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }

        Task savedTask = taskRepository.save(task);

        return convertToDTO(savedTask);
    }

    public List<TaskDTO> findAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> findTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return convertToDTO(task);
    }

    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setPriority(taskDTO.getPriority());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setDueDate(taskDTO.getDueDate());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());

        // Busca o usuário associado
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + taskDTO.getUserId()));
        task.setUser(user);

        return task;
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setCompletedAt(task.getCompletedAt());
        dto.setDueDate(task.getDueDate());
        dto.setUserId(task.getUser().getId());

        return dto;
    }

}
