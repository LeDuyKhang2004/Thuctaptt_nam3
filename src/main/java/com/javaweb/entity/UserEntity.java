package com.javaweb.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
// Entity này ánh xạ với bảng users trong database.
@Table(name = "users")
public class UserEntity {

    @Id
    // ID tự tăng, do MySQL sinh ra khi insert user mới.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Username không được trùng và không được null.
    @Column(unique = true, nullable = false)
    private String username;

    // Mật khẩu được lưu dưới dạng PBKDF2 kèm salt; độ dài 255 ký tự đủ cho chuỗi hash.
    @Column(nullable = false, length = 255)
    private String password;

    private String email;

    // Field fullName trong Java map với cột full_name trong DB.
    @Column(name = "full_name")
    private String fullName;

    // Vai trò dùng để phân quyền: EMPLOYEE hoặc ADMIN.
    private String role;

    // Một user có thể có nhiều đơn nghỉ phép.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LeaveRequestEntity> leaveRequests;

    // Getter dùng để đọc dữ liệu từ entity.
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public List<LeaveRequestEntity> getLeaveRequests() {
        return leaveRequests;
    }

    // Setter dùng để gán dữ liệu vào entity trước khi lưu DB.
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLeaveRequests(List<LeaveRequestEntity> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }
}
