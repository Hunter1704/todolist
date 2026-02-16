package com.example.todo_project.service;
import com.example.todo_project.dto.TaskRequestDto;
import com.example.todo_project.dto.TaskResponseDto;
import com.example.todo_project.model.TaskModel;
import com.example.todo_project.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository TaskRepository;
    public TaskService(TaskRepository TaskRepository) {
        this.TaskRepository = TaskRepository;
    }

    public TaskResponseDto getTaskById(String id) {
        TaskModel TaskModel = TaskRepository.findById(id).
                orElseThrow(()-> new RuntimeException("task not found"));
        return new TaskResponseDto(
                TaskModel.getId(),
                TaskModel.getName(),
                TaskModel.getDescription(),
                TaskModel.getStatus()
        );
    }
    public List<TaskResponseDto> getAllTasks() {
        List<TaskModel> TaskModels = TaskRepository.findAll();
        List<TaskResponseDto> TaskResponseDtos = new ArrayList<>();
        for (TaskModel TaskModel : TaskModels) {
            TaskResponseDtos.add(new TaskResponseDto(
                    TaskModel.getId(),
                    TaskModel.getName(),
                    TaskModel.getDescription(),
                    TaskModel.getStatus()
            ));
        }
        return TaskResponseDtos;
    }
    public TaskResponseDto addTask(TaskRequestDto TaskRequestDto) {
        TaskModel task = new TaskModel();
        task.setName(TaskRequestDto.getName());
        task.setDescription(TaskRequestDto.getDescription());
        task.setStatus(TaskRequestDto.getStatus());

        TaskModel saved = TaskRepository.save(task);

        return new TaskResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getStatus()
        );

    }
    public TaskResponseDto updateTask(String id, TaskRequestDto TaskRequestDto) {
        TaskModel task = TaskRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("task not found"));
        task.setName(TaskRequestDto.getName());
        task.setDescription(TaskRequestDto.getDescription());
        task.setStatus(TaskRequestDto.getStatus());
        TaskModel saved = TaskRepository.save(task);
        return new TaskResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getStatus()


        );
    }
    public void deleteTask(String id) {
        TaskModel  task = TaskRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("task not found"));
        TaskRepository.delete(task);
    }


}