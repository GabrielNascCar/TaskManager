package org.projeto.taskmanager.controller;

import jakarta.validation.Valid;
import org.projeto.taskmanager.dto.TaskDTO;
import org.projeto.taskmanager.dto.UserDTO;
import org.projeto.taskmanager.dto.UserPasswordUpdateDTO;
import org.projeto.taskmanager.dto.UserUpdateDTO;
import org.projeto.taskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final  UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDTO> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto
    ) {
        UserDTO updatedUser = userService.updateProfile(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UserPasswordUpdateDTO dto
    ) {
        userService.updatePassword(id, dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskDTO>> getUserTasks(@PathVariable Long userId) {
        List<TaskDTO> tasks = userService.getUserTasks(userId);
        return ResponseEntity.ok(tasks);
    }

}
