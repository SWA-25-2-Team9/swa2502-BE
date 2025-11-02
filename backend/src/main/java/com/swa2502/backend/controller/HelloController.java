package com.swa2502.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/login")
    public String login() {
        return "SWA2502 로그인";
    }
}