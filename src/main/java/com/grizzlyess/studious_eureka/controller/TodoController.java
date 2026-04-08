package com.grizzlyess.studious_eureka.controller;

import com.grizzlyess.studious_eureka.entity.Todo;
import com.grizzlyess.studious_eureka.service.TodoService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping()
    List<Todo> create(@RequestBody Todo todo){
        return todoService.create(todo);

    }

    @GetMapping
    List<Todo> list(){
        return todoService.list();

    }

    @PutMapping
    List<Todo> update(Todo todo){
        return todoService.update(todo);
    }

    @DeleteMapping("/{id}")
    List<Todo> delete(@PathVariable("id") UUID id){
        return todoService.delete(id);
    }
}
