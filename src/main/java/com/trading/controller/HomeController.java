package com.trading.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController { //différentes pages du site

    @GetMapping
    public String home(){
        return "Welcome to my trading platorm !";
    }

    @GetMapping("/api")
    public String secure(){
        return "Welcome to trading platform Secure";
    }
}
