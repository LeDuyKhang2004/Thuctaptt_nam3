package com.javaweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // URL gốc của website, ví dụ http://localhost:8080/leave_management/
    @GetMapping("/")
    public String index() {
        return "home";
    }

    // Trang giới thiệu chung, có link đăng nhập và đăng ký.
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
