package com.javaweb.controller;

import com.javaweb.entity.UserEntity;
import com.javaweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService users;
    public AuthController(UserService users) { this.users = users; }

    @GetMapping("/register")
    public String registerForm() { return "register"; }

    @PostMapping("/register")
    public String register(@ModelAttribute UserEntity submitted, Model model) {
        String name = submitted.getFullName() == null ? "" : submitted.getFullName().trim();
        String username = submitted.getUsername() == null ? "" : submitted.getUsername().trim();
        if (name.isEmpty() || username.length() < 3 || submitted.getPassword() == null || submitted.getPassword().length() < 8) {
            model.addAttribute("error", "Cần nhập họ tên, username từ 3 ký tự và mật khẩu từ 8 ký tự.");
            return "register";
        }
        if (users.findByUsername(username) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại."); return "register";
        }
        UserEntity safe = new UserEntity();
        safe.setFullName(name); safe.setUsername(username); safe.setPassword(submitted.getPassword());
        safe.setEmail(submitted.getEmail()); safe.setRole("EMPLOYEE");
        users.save(safe);
        return "redirect:/user/login?success=register";
    }

    @GetMapping({"/login", "/user/login", "/admin/login"})
    public String loginPage(HttpServletRequest request, Model model) {
        boolean admin = request.getRequestURI().endsWith("/admin/login");
        model.addAttribute("loginArea", admin ? "ADMIN" : "USER");
        return "login";
    }

    @PostMapping({"/login", "/user/login", "/admin/login"})
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        boolean adminArea = request.getRequestURI().endsWith("/admin/login");
        String loginUrl = adminArea ? "/admin/login" : "/user/login";
        UserEntity user = users.login(username, password);
        if (user == null) return "redirect:" + loginUrl + "?error=true";
        boolean admin = "ADMIN".equals(user.getRole());
        // Do not admit an employee into the Admin login area, or an Admin into User area.
        if (admin != adminArea) return "redirect:" + loginUrl + "?error=role";
        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute("currentUser", user);
        return admin ? "redirect:/admin/dashboard" : "redirect:/user/home";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean admin = session != null && session.getAttribute("currentUser") instanceof UserEntity u && "ADMIN".equals(u.getRole());
        if (session != null) session.invalidate();
        return admin ? "redirect:/admin/login" : "redirect:/user/login";
    }
}
