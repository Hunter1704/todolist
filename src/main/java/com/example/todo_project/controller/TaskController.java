package com.example.todo_project.controller;


import com.example.todo_project.dto.TaskRequestDto;
import com.example.todo_project.dto.TaskResponseDto;
import com.example.todo_project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    TaskService TaskService;

    @GetMapping
    public List<TaskResponseDto> getAllTasks() {
        return TaskService.getAllTasks();
    }
    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable String id) {
        return TaskService.getTaskById(id);
    }
    @PostMapping
    public TaskResponseDto addTask(@RequestBody TaskRequestDto TaskRequestDto) {
        return TaskService.addTask(TaskRequestDto);
    }
    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@RequestBody TaskRequestDto TaskRequestDto,@PathVariable String id) {
        return TaskService.updateTask(id,TaskRequestDto);
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        TaskService.deleteTask(id);
    }

}
