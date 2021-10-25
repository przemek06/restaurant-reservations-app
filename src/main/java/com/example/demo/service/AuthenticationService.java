package com.example.demo.service;

import com.example.demo.entity.UserDetailsEntity;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.RegistrationRequest;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.UserDetailsRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {
    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<String> authenticate(AuthenticationRequest authenticationRequest, HttpServletResponse response)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Bad credentials");
        }

        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = JwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("Authentication", "Bearer_" + jwt);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<String> register(RegistrationRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null) return new ResponseEntity<>("You are logged in!", HttpStatus.FORBIDDEN);
        request.setPassword(encoder.encode(request.getPassword()));
        UserDetailsEntity entity= new UserDetailsEntity(request);
        if(userRepository.findByUsername(entity.getUsername()).isPresent() ||
                userRepository.findByEmail(entity.getEmail()).isPresent())
        {
            return new ResponseEntity<>("There is already an user with this username or email: "+entity.getUsername(),
                    HttpStatus.BAD_REQUEST);
        } else {
            userRepository.save(entity);
            return new ResponseEntity<>("Registered username: "+entity.getUsername(), HttpStatus.OK);
        }
    }
}
