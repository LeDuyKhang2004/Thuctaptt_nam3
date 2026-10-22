package com.javaweb.controller;

import com.javaweb.entity.UserEntity;
import com.javaweb.service.LeaveRequestService;
import com.javaweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin") // Tất cả URL trong controller này bắt đầu bằng /admin.
public class AdminController {

    private final LeaveRequestService leaveRequestService;
    private final UserService userService;

    // Inject service để controller gọi xử lý nghiệp vụ.
    public AdminController(LeaveRequestService leaveRequestService, UserService userService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
    }

    // Kiểm tra người đang đăng nhập có phải ADMIN không.
    private boolean isAdmin(HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("currentUser");

        // Trim role để tránh lỗi nếu dữ liệu trong DB có khoảng trắng.
        return user != null
                && "ADMIN".equalsIgnoreCase(user.getRole() == null ? "" : user.getRole().trim());
    }

    // Hiển thị trang dashboard thống kê của admin.
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login"; // Không phải admin thì về trang login.
        }

        // Gửi số liệu thống kê sang dashboard.jsp.
        model.addAttribute("totalLeaves", leaveRequestService.countAll());
        model.addAttribute("pendingLeaves", leaveRequestService.countPending());
        model.addAttribute("approvedLeaves", leaveRequestService.countApproved());
        model.addAttribute("rejectedLeaves", leaveRequestService.countRejected());

        return "pages/admin/dashboard";
    }

    // Hiển thị danh sách đơn nghỉ phép, có thể lọc theo trạng thái.
    @GetMapping("/leaves")
    public String leaves(@RequestParam(value = "status", required = false) String status,
                         Model model,
                         HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        // Nếu không chọn trạng thái hoặc chọn ALL thì lấy tất cả đơn.
        if (status == null || status.trim().isEmpty() || "ALL".equals(status)) {
            model.addAttribute("leaves", leaveRequestService.findAll());
            model.addAttribute("selectedStatus", "ALL");
        } else {
            // Nếu có status thì lọc theo trạng thái: PENDING, APPROVED, REJECTED.
            model.addAttribute("leaves", leaveRequestService.findByStatus(status));
            model.addAttribute("selectedStatus", status);
        }

        return "pages/admin/leave-list";
    }

    // Hiển thị danh sách nhân viên, có thể tìm kiếm theo keyword.
    @GetMapping("/employees")
    public String employees(@RequestParam(value = "keyword", required = false) String keyword,
                            Model model,
                            HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        // Tìm kiếm user theo tên, username hoặc email.
        model.addAttribute("users", userService.searchUsers(keyword));
        model.addAttribute("keyword", keyword);

        return "pages/admin/employees";
    }

    // Admin duyệt đơn nghỉ phép.
    @GetMapping("/leaves/approve/{id}")
    public String approve(@PathVariable("id") Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        // Gọi service đổi trạng thái đơn thành APPROVED.
        leaveRequestService.approve(id);

        return "redirect:/admin/leaves?success=approve";
    }

    // Admin từ chối đơn nghỉ phép.
    @PostMapping("/leaves/reject/{id}")
    public String reject(@PathVariable("id") Long id,
                         @RequestParam("adminNote") String adminNote,
                         HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        // Gọi service đổi trạng thái đơn thành REJECTED và lưu lý do từ chối.
        leaveRequestService.reject(id, adminNote);

        return "redirect:/admin/leaves?success=reject";
    }

    // Admin cập nhật vai trò tài khoản: EMPLOYEE hoặc ADMIN.
    @PostMapping("/employees/update-role/{id}")
    public String updateRole(@PathVariable("id") Long id,
                             @RequestParam("role") String role,
                             HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser.getId().equals(id)) {
            return "redirect:/admin/employees?error=self-role";
        }

        // Cập nhật quyền của user.
        userService.updateRole(id, role);

        return "redirect:/admin/employees";
    }

    // Admin xem chi tiết một đơn nghỉ phép.
    @GetMapping("/leaves/detail/{id}")
    public String leaveDetail(@PathVariable("id") Long id,
                              Model model,
                              HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        // Tìm đơn theo id và gửi sang trang chi tiết.
        model.addAttribute("leave", leaveRequestService.findById(id));

        return "pages/admin/leave-detail";
    }
}
