package com.example.tmbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-master")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "✅ Backend is running and public endpoint works!";
    }
}
