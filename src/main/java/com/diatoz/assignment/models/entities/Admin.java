package com.diatoz.assignment.models.entities;

import com.diatoz.assignment.models.roles.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "admins")
public class Admin implements User{
    @Id
    private String id;

    @NotBlank
    @Size(min=6,max=20)
    private String username;

    @NotBlank
    @Size(max = 128)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private boolean enabled;

    @DBRef
    private Role role;

    public Admin(String username,String password,boolean enabled){
        this.username=username;
        this.password=password;
        this.enabled=enabled;
    }

    public Role getRole(){
        return this.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id) && Objects.equals(username, admin.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
