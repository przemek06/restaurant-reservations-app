package com.example.demo.entity;


import com.example.demo.model.RegistrationRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
@Table(name = "users")

public class UserDetailsEntity implements Serializable {

    public UserDetailsEntity(RegistrationRequest request) {
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.email = request.getEmail();
        this.active= true;
        this.role="ROLE_CLIENT";
    }

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name="email")
    @Email
    @Size(max=255)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private String role;
    @Column(name = "active")
    private Boolean active;
}
