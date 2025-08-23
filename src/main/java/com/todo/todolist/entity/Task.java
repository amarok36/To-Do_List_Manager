package com.todo.todolist.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Table("task")
public class Task {
    @Id
    private Long id;
    private String title;
    private String description;
    private String category;
    private String priority;
    private LocalDate deadlineDate;
    private boolean completed;
}