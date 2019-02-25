package com.posts.config;

import com.posts.entities.BlogEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;

@Configuration
public class ApplicationConfig {

    @Bean
    @Scope("singleton")
    public Object blogStore() {
        HashMap<String, BlogEntity> blogStore = new HashMap<>();
        return blogStore;
    }

}
