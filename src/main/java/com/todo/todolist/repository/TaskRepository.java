package com.todo.todolist.repository;

import com.todo.todolist.entity.Priority;
import com.todo.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategory(String category);
    List<Task> findByPriorityByPriority(Priority priority);
    List<Task> findByCompleted(boolean completed);
}
