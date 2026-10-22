package com.javaweb.controller;

import com.javaweb.entity.UserEntity;
import com.javaweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    // Inject UserService để xử lý đăng ký, đăng nhập và tìm user.
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Hiển thị form đăng ký.
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Xử lý đăng ký tài khoản.
    @PostMapping("/register")
    public String register(@ModelAttribute UserEntity user,
                           Model model) {

        String username = user.getUsername() == null ? "" : user.getUsername().trim();
        String fullName = user.getFullName() == null ? "" : user.getFullName().trim();

        if (username.length() < 3 || fullName.isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập họ tên và tên đăng nhập có ít nhất 3 ký tự.");
            model.addAttribute("user", user);
            return "register";
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự.");
            model.addAttribute("user", user);
            return "register";
        }

        user.setUsername(username);
        user.setFullName(fullName);

        // Kiểm tra username đã tồn tại chưa.
        UserEntity existingUser = userService.findByUsername(username);

        if (existingUser != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
            model.addAttribute("user", user);
            return "register";
        }

        // Nếu chưa trùng thì lưu user mới, role mặc định sẽ là EMPLOYEE.
        userService.save(user);

        return "redirect:/login?success=register";
    }

    // Hiển thị form đăng nhập.
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Xử lý đăng nhập.
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session) {

        // Service trả về user nếu username/password đúng, sai thì trả về null.
        UserEntity user = userService.login(username, password);

        if (user == null) {
            return "redirect:/login?error=true";
        }

        // Lưu thông tin user đang đăng nhập vào session.
        session.setAttribute("currentUser", user);

        // Điều hướng theo vai trò.
        if ("ADMIN".equalsIgnoreCase(user.getRole() == null ? "" : user.getRole().trim())) {
            return "redirect:/admin/dashboard";
        }

        return "redirect:/user/home";
    }

    // Đăng xuất: xóa toàn bộ session rồi quay về trang login.
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
