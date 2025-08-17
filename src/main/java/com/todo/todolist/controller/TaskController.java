package com.todo.todolist.controller;

import com.todo.todolist.entity.Task;
import com.todo.todolist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    @GetMapping
    public String listTasks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Boolean completed,
            Model model) {

        List<Task> tasks;
        if (category != null && !category.isEmpty()) {
            tasks = taskRepository.findByCategory(category);
        } else if (priority != null && !priority.isEmpty()) {
            tasks = taskRepository.findByPriority(priority);
        } else if (completed != null) {
            tasks = taskRepository.findByCompleted(completed);
        } else {
            tasks = taskRepository.findAll();
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentDate", LocalDate.now());
        return "task-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "task-form";
    }

    @PostMapping
    public String saveTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes) {
        taskRepository.save(task);
        redirectAttributes.addFlashAttribute("message", "Задача успешно сохранена!");
        return "redirect:/tasks";
    }
}
