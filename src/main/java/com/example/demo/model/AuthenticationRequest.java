package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull
    @Size(min=1, max=255)
    private String username;
    @NotNull
    @Size(min=1, max=255)
    private String password;

}
