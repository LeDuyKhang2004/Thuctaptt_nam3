package com.javaweb.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    // Trang chính của nhân viên sau khi đăng nhập thành công.
    @GetMapping("/user/home")
    public String userHome(HttpSession session) {
        // Bảo vệ trang user: chưa đăng nhập thì quay về login.
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }

        return "pages/users/home";
    }
}
