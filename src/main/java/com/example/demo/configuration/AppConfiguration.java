package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableScheduling
public class AppConfiguration {

    @Autowired
    SecurityConfiguration securityConfigurer;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return securityConfigurer.authenticationManagerBean();
    }
}
