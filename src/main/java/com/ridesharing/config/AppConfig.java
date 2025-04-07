package com.ridesharing.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.ridesharing")
@EnableJpaRepositories("com.ridesharing.repository")
public class AppConfig {
    // Configuration can be added here as needed
} 