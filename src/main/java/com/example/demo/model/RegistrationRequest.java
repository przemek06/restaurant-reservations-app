package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class RegistrationRequest {
    @Size(min=1, max=255)
    @NotNull
    private String username;
    @Size(min=1, max=255)
    @NotNull
    @Email
    private String email;
    @Size(min=1, max=255)
    @NotNull
    private String password;

}
