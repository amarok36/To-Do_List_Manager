package com.todo.todolist.repository;

import com.todo.todolist.entity.Task;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface TaskRepository extends R2dbcRepository<Task, Long> {
    Flux<Task> findByCategory(String category);
    Flux<Task> findByPriority(String priority);
    Flux<Task> findByCompleted(Boolean completed);
}

