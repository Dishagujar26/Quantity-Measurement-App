package com.qma.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigCheckRunner implements CommandLineRunner {

    @Value("${spring.datasource.url:NOT_FOUND}")
    private String url;

    @Value("${spring.datasource.username:NOT_FOUND}")
    private String username;

    @Value("${spring.datasource.password:NOT_FOUND}")
    private String password;

    @Override
    public void run(String... args) {
        System.out.println("DATASOURCE URL = " + url);
        System.out.println("DATASOURCE USERNAME = " + username);
        System.out.println("DATASOURCE PASSWORD = " + (password == null ? "null" : "[" + password + "]"));
    }
}