package com.grizzlyess.studious_eureka.repository;

import com.grizzlyess.studious_eureka.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
}
