package com.example.demo.service;

import com.example.demo.entity.UserDetailsEntity;
import com.example.demo.model.UserDetailsImpl;
import com.example.demo.repository.UserDetailsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserDetailsRepository userRepository;

    public UserDetailsServiceImpl(UserDetailsRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserDetailsEntity> user = userRepository.findByUsername(s);
        if(user.isPresent()){
            return new UserDetailsImpl(user.get());
        } else throw new UsernameNotFoundException("User doesn't exist");    }
}
