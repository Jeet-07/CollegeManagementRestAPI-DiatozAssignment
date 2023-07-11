package com.diatoz.assignment.payload.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDetailsBody {
    @NotBlank(message = "subject name must not be empty")
    private String name;
    @Size(min=0,max=100,message = "marks must be between 0 to 100")
    private double marks;
}
