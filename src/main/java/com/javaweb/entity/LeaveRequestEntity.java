package com.javaweb.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
// Entity này ánh xạ với bảng leave_requests trong database.
@Table(name = "leave_requests")
public class LeaveRequestEntity {

    @Id
    // ID tự tăng của từng đơn nghỉ phép.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lý do nghỉ có thể dài nên dùng TEXT trong MySQL.
    @Column(columnDefinition = "TEXT")
    private String reason;

    // Nhận dữ liệu từ input type="date" theo format yyyy-MM-dd.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    // Ngày kết thúc nghỉ phép.
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    // Trạng thái đơn: PENDING, APPROVED hoặc REJECTED.
    private String status;

    // Ghi chú của admin khi duyệt hoặc từ chối đơn.
    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    // Thời điểm user tạo đơn.
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Nhiều đơn nghỉ phép thuộc về một user.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne @JoinColumn(name="leave_type_id")
    private LeaveTypeEntity leaveType;
    @Column(name="work_days") private Integer workDays;
    @ManyToOne @JoinColumn(name="reviewed_by") private UserEntity reviewedBy;
    @Column(name="reviewed_at") private LocalDateTime reviewedAt;
    public LeaveTypeEntity getLeaveType(){return leaveType;}
    public void setLeaveType(LeaveTypeEntity leaveType){this.leaveType=leaveType;}
    public Integer getWorkDays(){return workDays;}
    public void setWorkDays(Integer value){this.workDays=value;}
    public UserEntity getReviewedBy(){return reviewedBy;}
    public void setReviewedBy(UserEntity value){this.reviewedBy=value;}
    public LocalDateTime getReviewedAt(){return reviewedAt;}
    public void setReviewedAt(LocalDateTime value){this.reviewedAt=value;}

    // Getter dùng để đọc dữ liệu từ entity.
    public Long getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity getUser() {
        return user;
    }

    // Setter dùng để gán dữ liệu vào entity trước khi lưu DB.
    public void setId(Long id) {
        this.id = id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
