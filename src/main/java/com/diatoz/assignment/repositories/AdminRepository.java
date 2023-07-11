package com.diatoz.assignment.repositories;

import com.diatoz.assignment.models.entities.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin,String> {
    Optional<Admin> findByUsername(String username);
    Boolean existsByUsername(String username);
}
