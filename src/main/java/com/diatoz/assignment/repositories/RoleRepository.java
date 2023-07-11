package com.diatoz.assignment.repositories;

import com.diatoz.assignment.models.roles.EnumRoles;
import com.diatoz.assignment.models.roles.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(EnumRoles name);
    Boolean existsByName(EnumRoles name);
}
