package com.diatoz.assignment.repositories;

import com.diatoz.assignment.models.entities.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher,String> {
    Optional<Teacher> findByUsername(String Username);
    Boolean existsByUsername(String Username);
}
