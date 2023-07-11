package com.diatoz.assignment.payload.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCreateBody {
    @NotBlank(message = "subName must not be empty")
    @Size(min=4,message = "subName must contain min 4 characters")
    private String subName;
}
