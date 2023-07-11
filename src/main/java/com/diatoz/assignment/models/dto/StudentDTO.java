package com.diatoz.assignment.models.dto;

import com.diatoz.assignment.models.entities.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String role;
    private String email;
    private String gender;
    private Set<Student.SubjectDetails> subjects;
}
