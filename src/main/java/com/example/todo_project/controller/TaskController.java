package com.example.todo_project.controller;


import com.example.todo_project.dto.TaskRequestDto;
import com.example.todo_project.dto.TaskResponseDto;
import com.example.todo_project.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }
    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }
    @PostMapping
    public TaskResponseDto addTask(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        return taskService.addTask(taskRequestDto);
    }
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@Valid @RequestBody TaskRequestDto taskRequestDto, @PathVariable String id) {
        return taskService.updateTask(id, taskRequestDto);
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
    }

}
