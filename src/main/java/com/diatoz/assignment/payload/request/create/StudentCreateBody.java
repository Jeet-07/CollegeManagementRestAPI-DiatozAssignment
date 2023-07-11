package com.diatoz.assignment.payload.request.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentCreateBody {
    @NotBlank(message="firstName must not be empty")
    private String firstName;
    @NotBlank(message="lastName must not be empty")
    private String lastName;
    @NotBlank(message="username must not be empty")
    @Size(min=5,max=20,message = "username must contain 5 to 20 characters")
    private String username;
    @NotBlank(message="password must not be empty")
    @Size(min=8,max=16,message = "password must contain 8 to 16 characters")
    private String password;
    private boolean enabled;

    @NotBlank(message = "gender must not be blank")
    @Pattern(regexp = "(?:m|M|male|Male|MALE|f|F|female|Female|FEMALE|o|other|Other|OTHER)$",
    message = "gender can only accept values from - m|M|male|Male|MALE|f|F|female|Female|FEMALE|o|other|Other|OTHER")
    private String gender;

    @NotBlank(message = "email must not empty")
    @Email(message = "email format not correct")
    private String email;

    private List<SubjectDetailsBody> subjectDetails;
}
