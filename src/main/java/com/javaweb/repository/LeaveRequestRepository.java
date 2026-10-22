package com.javaweb.repository;

import com.javaweb.entity.LeaveRequestEntity;
import com.javaweb.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository thao tác với bảng leave_requests thông qua entity LeaveRequestEntity.
public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {

    // Lấy tất cả đơn nghỉ phép thuộc về một user.
    List<LeaveRequestEntity> findByUser(UserEntity user);

    // Lọc đơn theo trạng thái: PENDING, APPROVED, REJECTED.
    List<LeaveRequestEntity> findByStatus(String status);

    // Đếm số đơn theo trạng thái để dùng cho dashboard admin.
    long countByStatus(String status);

    // Đếm toàn bộ đơn của một nhân viên để chatbot chỉ trả về dữ liệu đúng phạm vi.
    long countByUser(UserEntity user);

    // Đếm đơn của một nhân viên theo trạng thái cho chức năng tra cứu bằng chatbot.
    long countByUserAndStatus(UserEntity user, String status);
}
