package com.diatoz.assignment.payload.request.updation;

import com.diatoz.assignment.payload.request.create.SubjectDetailsBody;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentUpdateBody {
    private String firstName;
    private String lastName;
    @Size(min=5,max=20,message = "username must contain 5 to 20 characters or empty")
    private String username;

    @Size(min=8,max=16,message = "password must contain 8 to 16 characters or empty")
    private String password;
    private boolean enabled;

    @Pattern(regexp = "(?:m|M|male|Male|MALE|f|F|female|Female|FEMALE|o|other|Other|OTHER)$",
            message = "gender can only accept values from - m|M|male|Male|MALE|f|F|female|Female|FEMALE|o|other|Other|OTHER")
    private String gender;

    @Email(message = "email format not correct")
    private String email;

    private List<SubjectDetailsBody> subjectDetails;
}
