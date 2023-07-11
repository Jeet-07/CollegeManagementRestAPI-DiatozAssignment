package com.diatoz.assignment.payload.request.updation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherUpdateBody {
    private String firstName;
    private String lastName;

    @Size(min=5,max=20,message="username must contain 5-20 characters or empty")
    private String username;

    @Size(min=8,max=20,message = "password must contain 5-20 characters or empty")
    private String password;

    @Email(message = "email format not correct")
    private String email;

    private List<String> subjects;
}
