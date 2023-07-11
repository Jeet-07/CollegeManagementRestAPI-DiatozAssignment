package com.diatoz.assignment.repositories;

import com.diatoz.assignment.models.entities.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubjectRepository extends MongoRepository<Subject,String> {
    boolean existsByName(String name);
    Subject findByName(String name);
}
