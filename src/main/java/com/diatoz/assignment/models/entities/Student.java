package com.diatoz.assignment.models.entities;

import com.diatoz.assignment.models.roles.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Document(collection = "students")
public class Student implements User{
    @Id private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private boolean enabled;

    @DBRef
    private Role role;

    @Getter
    @Setter
    @Document
    public static class SubjectDetails{
        @DBRef
        private Subject subject;

        @NotBlank
        @Size(min=0,max=100)
        private Double marks;

        public SubjectDetails(Subject subject,Double marks){
            this.subject=subject;
            this.marks=marks;
        }
    }

    private Set<Student.SubjectDetails> subjects = new HashSet<>();

    public Role getRole(){
        return this.role;
    }
}
