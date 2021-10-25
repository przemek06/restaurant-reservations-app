package com.example.demo.controller;

import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.RegistrationRequest;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest,
                                               HttpServletResponse response) throws Exception {
        return authenticationService.authenticate(authenticationRequest, response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return authenticationService.register(registrationRequest);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> constraintViolation(ConstraintViolationException ex){
        return ResponseEntity.badRequest().body(ex.getConstraintViolations().stream().findFirst().get().getMessage());
    }
}
