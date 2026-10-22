package com.javaweb.service;

import com.javaweb.entity.LeaveRequestEntity;
import com.javaweb.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatbotServiceTest {

    private final ChatbotService chatbotService = new ChatbotService(new StubLeaveRequestService());

    @Test
    void employeeReceivesOnlyPersonalCounts() {
        String answer = chatbotService.reply("Tôi có bao nhiêu đơn đang chờ duyệt?", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("2 đơn"));
        assertTrue(answer.contains("của bạn"));
        assertFalse(answer.contains("toàn hệ thống"));
    }

    @Test
    void adminReceivesSystemCounts() {
        String answer = chatbotService.reply("Thống kê số lượng đơn", user(99L, "ADMIN", "Quản trị viên"));

        assertTrue(answer.contains("Tổng cộng: 20 đơn"));
        assertTrue(answer.contains("Chờ duyệt: 5"));
        assertTrue(answer.contains("trên toàn hệ thống"));
    }

    @Test
    void recognizesVietnameseStatusQuestionWithoutAccents() {
        String answer = chatbotService.reply("PENDING nghia la gi?", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("PENDING (Chờ duyệt)"));
        assertTrue(answer.contains("có thể hủy"));
    }

    @Test
    void doesNotConfuseNotApprovedWithApproved() {
        String answer = chatbotService.reply("Đơn không được duyệt nghĩa là gì?", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("REJECTED (Từ chối)"));
        assertFalse(answer.contains("APPROVED (Đã duyệt)"));
    }

    @Test
    void givesRoleAwareSuggestedAction() {
        String employeeAnswer = chatbotService.reply("Tôi nên làm gì tiếp theo?", user(1L, "EMPLOYEE", "Người kiểm thử"));
        String adminAnswer = chatbotService.reply("Gợi ý thao tác", user(99L, "ADMIN", "Quản trị viên"));

        assertTrue(employeeAnswer.contains("Đơn của tôi"));
        assertTrue(adminAnswer.contains("5 đơn đang chờ duyệt"));
        assertTrue(adminAnswer.contains("Quản lý đơn nghỉ phép"));
    }

    @Test
    void employeeCanInspectOwnLeaveById() {
        String answer = chatbotService.reply("Kiểm tra đơn số 10", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("Đơn #10"));
        assertTrue(answer.contains("3 ngày"));
        assertTrue(answer.contains("Ghi chú Admin: Đã xác nhận lịch bàn giao"));
    }

    @Test
    void employeeCannotInspectAnotherUsersLeave() {
        String answer = chatbotService.reply("Xem đơn số 20", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("không thuộc tài khoản của bạn"));
        assertFalse(answer.contains("Việc riêng của người khác"));
    }

    @Test
    void adminCanInspectAnyLeaveAndSeesEmployeeName() {
        String answer = chatbotService.reply("Chi tiết đơn số 20", user(99L, "ADMIN", "Quản trị viên"));

        assertTrue(answer.contains("Đơn #20"));
        assertTrue(answer.contains("Nhân viên: Người dùng khác"));
        assertTrue(answer.contains("Việc riêng của người khác"));
    }

    @Test
    void latestLeaveUsesNewestCreatedAt() {
        String answer = chatbotService.reply("Đơn gần nhất của tôi", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("Đơn #11"));
        assertTrue(answer.contains("PENDING (Chờ duyệt)"));
    }

    @Test
    void calculatesApprovedDaysInclusively() {
        String answer = chatbotService.reply("Tôi đã nghỉ bao nhiêu ngày trong năm nay?", user(1L, "EMPLOYEE", "Người kiểm thử"));

        assertTrue(answer.contains("1 đơn"));
        assertTrue(answer.contains("3 ngày nghỉ"));
    }

    private UserEntity user(Long id, String role, String fullName) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername("tester" + id);
        user.setFullName(fullName);
        user.setRole(role);
        return user;
    }

    private static class StubLeaveRequestService extends LeaveRequestService {

        private final List<LeaveRequestEntity> leaves;

        StubLeaveRequestService() {
            super(null);

            UserEntity employee = testUser(1L, "Người kiểm thử");
            UserEntity otherEmployee = testUser(2L, "Người dùng khác");

            leaves = List.of(
                    leave(10L, employee, "APPROVED", "Nghỉ chăm sóc gia đình",
                            "Đã xác nhận lịch bàn giao", LocalDate.of(2026, 8, 10),
                            LocalDate.of(2026, 8, 12), LocalDateTime.of(2026, 8, 1, 8, 0)),
                    leave(11L, employee, "PENDING", "Khám sức khỏe", null,
                            LocalDate.of(2026, 8, 20), LocalDate.of(2026, 8, 20),
                            LocalDateTime.of(2026, 8, 15, 9, 30)),
                    leave(20L, otherEmployee, "REJECTED", "Việc riêng của người khác",
                            "Trùng lịch dự án", LocalDate.of(2026, 7, 2),
                            LocalDate.of(2026, 7, 2), LocalDateTime.of(2026, 7, 1, 10, 0))
            );
        }

        @Override
        public List<LeaveRequestEntity> findAll() {
            return leaves;
        }

        @Override
        public List<LeaveRequestEntity> findByUser(UserEntity user) {
            return leaves.stream()
                    .filter(leave -> leave.getUser().getId().equals(user.getId()))
                    .toList();
        }

        @Override
        public LeaveRequestEntity findById(Long id) {
            return leaves.stream()
                    .filter(leave -> leave.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public long countAll() {
            return 20;
        }

        @Override
        public long countPending() {
            return 5;
        }

        @Override
        public long countApproved() {
            return 12;
        }

        @Override
        public long countRejected() {
            return 3;
        }

        @Override
        public long countByUser(UserEntity user) {
            return 4;
        }

        @Override
        public long countByUserAndStatus(UserEntity user, String status) {
            return switch (status) {
                case "PENDING" -> 2;
                case "APPROVED" -> 1;
                case "REJECTED" -> 1;
                default -> 0;
            };
        }

        private static UserEntity testUser(Long id, String fullName) {
            UserEntity user = new UserEntity();
            user.setId(id);
            user.setUsername("user" + id);
            user.setFullName(fullName);
            user.setRole("EMPLOYEE");
            return user;
        }

        private static LeaveRequestEntity leave(Long id,
                                                UserEntity user,
                                                String status,
                                                String reason,
                                                String adminNote,
                                                LocalDate startDate,
                                                LocalDate endDate,
                                                LocalDateTime createdAt) {
            LeaveRequestEntity leave = new LeaveRequestEntity();
            leave.setId(id);
            leave.setUser(user);
            leave.setStatus(status);
            leave.setReason(reason);
            leave.setAdminNote(adminNote);
            leave.setStartDate(startDate);
            leave.setEndDate(endDate);
            leave.setCreatedAt(createdAt);
            return leave;
        }
    }
}
