package com.example.todo_project.model;

import com.example.todo_project.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")

public class TaskModel {
    @Id
    private String id;

    private String name;
    private String description;
    private TaskStatus status;
}
