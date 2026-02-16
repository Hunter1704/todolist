package com.example.todo_project.repository;


import com.example.todo_project.model.TaskModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskModel, String> {

}