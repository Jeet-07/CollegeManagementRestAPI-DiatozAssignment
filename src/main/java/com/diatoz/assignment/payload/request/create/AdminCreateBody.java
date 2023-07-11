package com.diatoz.assignment.payload.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateBody {
    @NotBlank(message = "username must not be blank")
    @Size(min=5,max=20,message = "username must contain 5 to 20 characters")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min=8,max=16,message = "password must contain 8 to 16 characters")
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
}
