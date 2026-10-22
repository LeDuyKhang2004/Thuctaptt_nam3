package com.javaweb.service;

import com.javaweb.entity.LeaveRequestEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;

    // Inject repository để service thao tác với bảng leave_requests.
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    // Lấy tất cả đơn nghỉ phép cho admin xem.
    public List<LeaveRequestEntity> findAll() {
        return leaveRequestRepository.findAll();
    }

    // Lấy các đơn của một nhân viên cụ thể.
    public List<LeaveRequestEntity> findByUser(UserEntity user) {
        return leaveRequestRepository.findByUser(user);
    }

    // Tìm đơn theo id; không thấy thì trả về null.
    public LeaveRequestEntity findById(Long id) {
        return leaveRequestRepository.findById(id).orElse(null);
    }

    // Tạo đơn mới: gắn user, set trạng thái chờ duyệt và ngày tạo.
    public LeaveRequestEntity createLeaveRequest(LeaveRequestEntity leaveRequest, UserEntity user) {
        leaveRequest.setUser(user); // gán đơn này cho nv hiện tại đnag đn
        leaveRequest.setStatus("PENDING"); //trạng thái Pending (chờ duyệt từ phía admin)
        leaveRequest.setCreatedAt(LocalDateTime.now()); //lưu tgian tạo

        return leaveRequestRepository.save(leaveRequest);
    }

    // Admin duyệt đơn: chỉ xử lý khi đơn đang ở trạng thái PENDING.
    public void approve(Long id) {
        LeaveRequestEntity leaveRequest = findById(id);

        if (leaveRequest != null && "PENDING".equals(leaveRequest.getStatus())) {
            leaveRequest.setStatus("APPROVED");
            leaveRequest.setAdminNote("Đơn đã được duyệt");
            leaveRequestRepository.save(leaveRequest);
        }
    }

    // Admin từ chối đơn và lưu lý do từ chối.
    public void reject(Long id, String adminNote) {
        LeaveRequestEntity leaveRequest = findById(id);

        if (leaveRequest != null && "PENDING".equals(leaveRequest.getStatus())) {
            leaveRequest.setStatus("REJECTED");

            // Nếu admin không nhập lý do thì dùng nội dung mặc định.
            if (adminNote == null || adminNote.trim().isEmpty()) {
                leaveRequest.setAdminNote("Không được duyệt");
            } else {
                leaveRequest.setAdminNote(adminNote.trim());
            }

            leaveRequestRepository.save(leaveRequest);
        }
    }

    // Xóa đơn theo id, hiện chưa dùng trực tiếp ở controller.
    public void delete(Long id) {
        leaveRequestRepository.deleteById(id);
    }

    // Đếm tổng số đơn để hiển thị dashboard admin.
    public long countAll() {
        return leaveRequestRepository.count();
    }

    // Đếm số đơn đang chờ duyệt.
    public long countPending() {
        return leaveRequestRepository.countByStatus("PENDING");
    }

    // Đếm số đơn đã duyệt.
    public long countApproved() {
        return leaveRequestRepository.countByStatus("APPROVED");
    }

    // Đếm số đơn bị từ chối.
    public long countRejected() {
        return leaveRequestRepository.countByStatus("REJECTED");
    }

    // Đếm tổng số đơn thuộc về một nhân viên.
    public long countByUser(UserEntity user) {
        return leaveRequestRepository.countByUser(user);
    }

    // Đếm số đơn của một nhân viên theo trạng thái.
    public long countByUserAndStatus(UserEntity user, String status) {
        return leaveRequestRepository.countByUserAndStatus(user, status);
    }

    // Lọc danh sách đơn theo trạng thái.
    public List<LeaveRequestEntity> findByStatus(String status) {
        return leaveRequestRepository.findByStatus(status);
    }

    // Nhân viên chỉ được hủy đơn của chính mình và đơn phải còn PENDING.
    public void cancelLeaveRequest(Long id, UserEntity currentUser) {
        LeaveRequestEntity leaveRequest = findById(id);

        if (leaveRequest != null
                && leaveRequest.getUser().getId().equals(currentUser.getId())
                && "PENDING".equals(leaveRequest.getStatus())) {

            leaveRequestRepository.deleteById(id);
        }
    }
}
