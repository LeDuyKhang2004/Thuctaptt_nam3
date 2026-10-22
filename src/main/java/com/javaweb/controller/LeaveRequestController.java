package com.javaweb.controller;

import com.javaweb.entity.LeaveRequestEntity;
import com.javaweb.entity.UserEntity;
import com.javaweb.service.LeaveRequestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    // Inject service xử lý nghiệp vụ đơn nghỉ phép cho controller.
    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    //  Tạo đơn nghỉ phép cho nhân viên đã đăng nhập.
    @GetMapping("/leave/create")
    public String showCreateForm(HttpSession session) {
        // Nếu chưa đăng nhập thì không được vào form tạo đơn.
        if (session.getAttribute("currentUser") == null) {
            return "redirect:/login";
        }

        return "pages/users/leave-create";
    }

    // Nhận dữ liệu từ form tạo đơn và lưu đơn nghỉ phép mới.
    @PostMapping("/leave/create")
    public String createLeaveRequest(@ModelAttribute LeaveRequestEntity leaveRequest, //Spring tự lấy dữ liệu từ form rồi đổ vào object LeaveRequestEntity
                                     HttpSession session,
                                     Model model) {

        // kieemr tra nv đã đn chưa, đn thì mới đc tạo đơn
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Validate lý do nghỉ: bắt buộc nhập tối thiểu 5 ký tự.
        if (leaveRequest.getReason() == null || leaveRequest.getReason().trim().length() < 5) {
            model.addAttribute("error", "Lý do nghỉ phải có ít nhất 5 ký tự.");
            model.addAttribute("leaveRequest", leaveRequest);
            return "pages/users/leave-create";
        }

        // Validate ngày bắt đầu và ngày kết thúc không được bỏ trống.
        if (leaveRequest.getStartDate() == null || leaveRequest.getEndDate() == null) {
            model.addAttribute("error", "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            model.addAttribute("leaveRequest", leaveRequest);
            return "pages/users/leave-create";
        }

        // Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.
        if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
            model.addAttribute("error", "Ngày kết thúc không được nhỏ hơn ngày bắt đầu.");
            model.addAttribute("leaveRequest", leaveRequest);
            return "pages/users/leave-create";
        }

        // Gọi service để set user, trạng thái PENDING, ngày tạo rồi lưu xuống DB.
        leaveRequestService.createLeaveRequest(leaveRequest, currentUser);

        return "redirect:/leave/my?success=create";
    }

    // Hiển thị danh sách đơn nghỉ phép của nhân viên đang đăng nhập.
    @GetMapping("/leave/my")
    public String myLeaves(Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Chỉ lấy đơn của currentUser, không lấy đơn của người khác.
        model.addAttribute("leaves", leaveRequestService.findByUser(currentUser));

        return "pages/users/my-leaves";
    }

    // Nhân viên hủy đơn nghỉ phép của mình khi đơn vẫn còn PENDING.
    @GetMapping("/leave/cancel/{id}")
    public String cancelLeave(@PathVariable("id") Long id,
                              HttpSession session) {

        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Service sẽ kiểm tra đơn có thuộc về currentUser và còn PENDING hay không.
        leaveRequestService.cancelLeaveRequest(id, currentUser);

        return "redirect:/leave/my";
    }

    // Nhân viên xem chi tiết một đơn nghỉ phép của chính mình.
    @GetMapping("/leave/detail/{id}")
    public String leaveDetail(@PathVariable("id") Long id,
                              Model model,
                              HttpSession session) {

        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Tìm đơn theo id trên URL.
        LeaveRequestEntity leaveRequest = leaveRequestService.findById(id);

        if (leaveRequest == null) {
            return "redirect:/leave/my";
        }

        // Nhân viên chỉ được xem đơn của chính mình.
        if (!leaveRequest.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/leave/my";
        }

        // Gửi đơn sang JSP để hiển thị thông tin chi tiết.
        model.addAttribute("leave", leaveRequest);

        return "pages/users/leave-detail";
    }
}
