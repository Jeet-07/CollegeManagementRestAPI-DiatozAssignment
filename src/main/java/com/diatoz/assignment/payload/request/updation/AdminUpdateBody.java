package com.diatoz.assignment.payload.request.updation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateBody {
    @NotBlank
    @Size(min=5,max=20,message = "username must contain 5 to 20 characters")
    private String username;

    @NotBlank
    @Size(min=8,max=16,message = "password must contain 8 to 16 characters")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
