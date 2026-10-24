package com.javaweb.controller;

import com.javaweb.entity.*;
import com.javaweb.service.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.*;

@Controller
public class AdditionalController {
    private final ActivityService activity;
    private final LeaveRequestService leaves;
    private final LeavePolicyService policy;
    public AdditionalController(ActivityService a, LeaveRequestService l, LeavePolicyService p) {
        this.activity=a; this.leaves=l; this.policy=p;
    }
    private UserEntity current(HttpSession session) {return (UserEntity)session.getAttribute("currentUser");}
    private boolean admin(UserEntity u) {return u!=null && "ADMIN".equals(u.getRole());}
    private String inbox(UserEntity u){return admin(u)?"/admin/notifications":"/user/notifications";}

    @GetMapping({"/notifications","/user/notifications","/admin/notifications"})
    public String notifications(HttpSession session,Model model) {
        UserEntity user=current(session); if(user==null) return "redirect:/user/login";
        model.addAttribute("notices",activity.forUser(user));
        model.addAttribute("unread",activity.unread(user));
        return "pages/common/notifications";
    }
    @PostMapping("/notifications/read/{id}")
    public String read(@PathVariable Long id,HttpSession session) {
        UserEntity user=current(session);if(user==null)return "redirect:/user/login";
        activity.markRead(id,user);return "redirect:"+inbox(user);
    }
    @PostMapping("/notifications/delete/{id}")
    public String delete(@PathVariable Long id,HttpSession session) {
        UserEntity user=current(session);if(user==null)return "redirect:/user/login";
        boolean done=activity.deleteIfRead(id,user);
        return "redirect:"+inbox(user)+(done?"?success=deleted":"?error=read-first");
    }
    @PostMapping("/notifications/delete-read")
    public String deleteRead(HttpSession session) {
        UserEntity user=current(session);if(user==null)return "redirect:/user/login";
        activity.deleteAllRead(user);return "redirect:"+inbox(user)+"?success=deleted";
    }
    @GetMapping("/leave/balance")
    public String balance(HttpSession session,Model model) {
        UserEntity u=current(session); if(u==null)return "redirect:/user/login";
        int year=LocalDate.now().getYear();
        model.addAttribute("year",year);model.addAttribute("balance",policy.balance(u,year));
        model.addAttribute("reserved",policy.reservedAnnualDays(u,year));
        model.addAttribute("remaining",policy.remaining(u,year));
        return "pages/users/balance";
    }
    @GetMapping({"/calendar","/user/calendar","/admin/calendar"})
    public String calendar(HttpSession session,Model model) {
        UserEntity u=current(session);if(u==null)return "redirect:/user/login";
        model.addAttribute("leaves",admin(u)?leaves.findByStatus("APPROVED"):
                            leaves.findByUser(u).stream().filter(l->"APPROVED".equals(l.getStatus())).toList());
        model.addAttribute("holidays",policy.holidays());
        return "pages/common/calendar";
    }
    @GetMapping("/admin/reports")
    public String reports(HttpSession s,Model m,@RequestParam(required=false)Integer year) {
        UserEntity u=current(s);if(!admin(u))return "redirect:/admin/login";
        int chosen=year==null?LocalDate.now().getYear():year;
        m.addAttribute("year",chosen);
        m.addAttribute("leaves",leaves.findAll().stream().filter(l->l.getStartDate()!=null&&l.getStartDate().getYear()==chosen).toList());
        return "pages/admin/reports";
    }
    @GetMapping(value="/admin/reports/export",produces="text/csv;charset=UTF-8")
    public void csv(HttpSession s,HttpServletResponse response,@RequestParam(required=false)Integer year) throws java.io.IOException {
        UserEntity u=current(s);if(!admin(u)){response.sendError(403);return;}
        int y=year==null?LocalDate.now().getYear():year;
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment; filename=leave-report-"+y+".csv");
        try(var out=response.getWriter()){
            out.print('\uFEFF');
            out.println("ID,Nhân viên,Loại phép,Từ ngày,Đến ngày,Số ngày,Trạng thái");
            for(LeaveRequestEntity l:leaves.findAll())if(l.getStartDate()!=null&&l.getStartDate().getYear()==y)
                out.println(l.getId()+","+cell(l.getUser().getFullName())+","+cell(l.getLeaveType()==null?"Cũ":l.getLeaveType().getName())+","+
                  cell(l.getStartDate().toString())+","+cell(l.getEndDate().toString())+","+
                  (l.getWorkDays()==null?0:l.getWorkDays())+","+cell(l.getStatus()));
        }
    }
    private String cell(String val) {
        String s=val==null?"":val.replace("\"","\"\"");
        if(s.matches("^[=+@\\-\\t\\r].*"))s="'"+s;
        return "\""+s+"\"";
    }
}
