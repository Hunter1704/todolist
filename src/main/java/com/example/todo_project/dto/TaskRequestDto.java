package com.example.todo_project.dto;


import com.example.todo_project.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class TaskRequestDto {
    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Description required")
    @Size(min = 1, max = 50, message = "Description can't exceed 50 characters")
    private String description;

    @NotBlank(message = "Status is required")
    private TaskStatus status;
}
