package com.example.todo_project.dto;

import com.example.todo_project.entity.TaskStatus;


public record TaskResponseDto(String id, String name, String description, TaskStatus status){
}
