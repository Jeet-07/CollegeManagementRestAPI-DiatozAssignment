package com.diatoz.assignment.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String role;
}
