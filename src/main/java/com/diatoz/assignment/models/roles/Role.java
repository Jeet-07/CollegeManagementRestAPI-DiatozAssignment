package com.diatoz.assignment.models.roles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private EnumRoles name;

    public Role(EnumRoles name){
        this.name=name;
    }
}
