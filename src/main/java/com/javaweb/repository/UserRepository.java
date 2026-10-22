package com.javaweb.repository;

import com.javaweb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository đã có sẵn các hàm CRUD như save, findById, findAll, deleteById.
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // tìm user theo username.
    UserEntity findByUsername(String username);

    // Tìm kiếm gần đúng, không phân biệt hoa thường trên fullName, username hoặc email.
    List<UserEntity> findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String fullName,
            String username,
            String email
    );
}
