package com.todo.todolist.controller;

import com.todo.todolist.entity.Task;
import com.todo.todolist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    @GetMapping
    public Mono<String> listTasks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Boolean completed,
            Model model) {

        Mono<List<Task>> tasksMono;

        if (category != null && !category.isEmpty()) {
            tasksMono = taskRepository.findByCategory(category).collectList();
        } else if (priority != null && !priority.isEmpty()) {
            tasksMono = taskRepository.findByPriority(priority).collectList();
        } else if (completed != null) {
            tasksMono = taskRepository.findByCompleted(completed).collectList();
        } else {
            tasksMono = taskRepository.findAll().collectList();
        }

        return tasksMono.flatMap(tasks -> {
            model.addAttribute("tasks", tasks);
            model.addAttribute("currentDate", LocalDate.now());
            return Mono.just("task-list");
        });
    }

    @GetMapping("/add")
    public Mono<String> showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return Mono.just("task-form");
    }

    @PostMapping("/save")
    public Mono<String> saveTask(@ModelAttribute Task task, WebSession session) {
        return taskRepository.save(task)
                .then(Mono.fromRunnable(() ->
                        session.getAttributes().put("message", "Задача сохранена")))
                .thenReturn("redirect:/tasks");
    }

    @GetMapping("/edit/{id}")
    public Mono<String> showEditForm(@PathVariable Long id, Model model) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Неверный ID задачи: " + id)))
                .flatMap(task -> {
                    model.addAttribute("task", task);
                    return Mono.just("task-form");
                });
    }

    @GetMapping("/delete/{id}")
    public Mono<String> deleteTask(@PathVariable Long id, WebSession session) {
        return taskRepository.deleteById(id)
                .then(Mono.fromRunnable(() ->
                        session.getAttributes().put("message", "Задача удалена")))
                .thenReturn("redirect:/tasks");
    }

    @GetMapping("/complete/{id}")
    public Mono<String> toggleTaskCompletion(@PathVariable Long id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Неверный ID задачи: " + id)))
                .flatMap(task -> {
                    task.setCompleted(!task.isCompleted());
                    return taskRepository.save(task);
                })
                .thenReturn("redirect:/tasks");
    }
}