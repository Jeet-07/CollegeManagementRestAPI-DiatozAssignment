package com.diatoz.assignment.repositories;

import com.diatoz.assignment.models.entities.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student,String> {
    Optional<Student> findByUsername(String username);
    Boolean existsByUsername(String username);
}
