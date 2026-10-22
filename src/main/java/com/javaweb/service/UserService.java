package com.javaweb.service;

import com.javaweb.entity.UserEntity;
import com.javaweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    // Inject repository để service có thể thao tác với bảng users.
    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    // Lưu user mới; nếu chưa có role thì mặc định là EMPLOYEE.
    public UserEntity save(UserEntity user) {
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("EMPLOYEE");
        }

        if (!passwordService.isEncoded(user.getPassword())) {
            user.setPassword(passwordService.hash(user.getPassword()));
        }

        return userRepository.save(user);
    }

    // Tìm user theo username, dùng khi đăng ký và đăng nhập.
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Tìm user theo id; không có thì trả về null cho controller/service dễ kiểm tra.
    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Lấy toàn bộ user trong bảng users.
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    // Kiểm tra đăng nhập bằng username và password.
    public UserEntity login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        // Trim username để tránh lỗi do nhập thừa khoảng trắng đầu/cuối.
        UserEntity user = userRepository.findByUsername(username.trim());

        if (user != null && passwordService.matches(password, user.getPassword())) {
            // Tự động nâng cấp mật khẩu plain text của dữ liệu cũ sau lần đăng nhập hợp lệ đầu tiên.
            if (!passwordService.isEncoded(user.getPassword())) {
                user.setPassword(passwordService.hash(password));
                userRepository.save(user);
            }
            return user;
        }

        return null;
    }

    // Admin tìm kiếm nhân viên theo họ tên, username hoặc email.
    public List<UserEntity> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll();
        }

        String searchKeyword = keyword.trim();

        return userRepository
                .findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        searchKeyword,
                        searchKeyword,
                        searchKeyword
                );
    }

    // Admin đổi vai trò tài khoản giữa EMPLOYEE và ADMIN.
    public void updateRole(Long userId, String role) {
        UserEntity user = findById(userId);
        String normalizedRole = role == null ? "" : role.trim().toUpperCase();

        if (user != null && ("ADMIN".equals(normalizedRole) || "EMPLOYEE".equals(normalizedRole))) {
            user.setRole(normalizedRole);
            userRepository.save(user);
        }
    }
}
