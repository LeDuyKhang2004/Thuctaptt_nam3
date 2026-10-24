package com.javaweb.controller;
import com.javaweb.entity.*;
import com.javaweb.service.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@Controller @RequestMapping("/admin")
public class AdminController {
 private final LeaveRequestService leaves;private final UserService users;
 private final LeavePolicyService policy;private final ActivityService activity;private final DepartmentService departments;
 public AdminController(LeaveRequestService l,UserService u,LeavePolicyService p,ActivityService a,DepartmentService d){leaves=l;users=u;policy=p;activity=a;departments=d;}
 private boolean isAdmin(HttpSession s){UserEntity u=(UserEntity)s.getAttribute("currentUser");return u!=null&&"ADMIN".equals(u.getRole());}
 @GetMapping("/dashboard") public String dashboard(Model m,HttpSession s){if(!isAdmin(s))return "redirect:/login";m.addAttribute("totalLeaves",leaves.countAll());m.addAttribute("pendingLeaves",leaves.countPending());m.addAttribute("approvedLeaves",leaves.countApproved());m.addAttribute("rejectedLeaves",leaves.countRejected());m.addAttribute("leaves",leaves.findAll());return "pages/admin/dashboard";}
 @GetMapping("/leaves") public String list(@RequestParam(required=false) String status,Model m,HttpSession s){if(!isAdmin(s))return "redirect:/login";String selected=(status==null||status.isBlank())?"ALL":status; m.addAttribute("leaves","ALL".equals(selected)?leaves.findAll():leaves.findByStatus(selected));m.addAttribute("selectedStatus",selected);return "pages/admin/leave-list";}
 @GetMapping("/employees") public String employees(@RequestParam(required=false)String keyword,Model m,HttpSession s){if(!isAdmin(s))return "redirect:/login";m.addAttribute("users",users.searchUsers(keyword));m.addAttribute("keyword",keyword);m.addAttribute("departments",departments.all());return "pages/admin/employees";}
 @PostMapping("/leaves/approve/{id}") public String approve(@PathVariable Long id,HttpSession s){if(!isAdmin(s))return "redirect:/login";try{leaves.approve(id,(UserEntity)s.getAttribute("currentUser"));}catch(IllegalArgumentException e){return "redirect:/admin/leaves?error=invalid";}return "redirect:/admin/leaves?success=approve";}
 @PostMapping("/leaves/reject/{id}") public String reject(@PathVariable Long id,@RequestParam String adminNote,HttpSession s){if(!isAdmin(s))return "redirect:/login";try{leaves.reject(id,adminNote,(UserEntity)s.getAttribute("currentUser"));}catch(IllegalArgumentException e){return "redirect:/admin/leaves?error=invalid";}return "redirect:/admin/leaves?success=reject";}
 @PostMapping("/employees/update-role/{id}") public String role(@PathVariable Long id,@RequestParam String role,HttpSession s){if(!isAdmin(s))return "redirect:/login";UserEntity u=(UserEntity)s.getAttribute("currentUser");if(u.getId().equals(id))return "redirect:/admin/employees?error=self-role";users.updateRole(id,role);activity.log(u,"UPDATE_ROLE",id,role);return "redirect:/admin/employees";}
 @GetMapping("/leaves/detail/{id}") public String detail(@PathVariable Long id,Model m,HttpSession s){if(!isAdmin(s))return "redirect:/login";m.addAttribute("leave",leaves.findById(id));return "pages/admin/leave-detail";}
 @GetMapping("/settings")public String settings(Model m,HttpSession s){if(!isAdmin(s))return "redirect:/login";m.addAttribute("users",users.findAll());m.addAttribute("departments",departments.all());m.addAttribute("holidays",policy.holidays());m.addAttribute("year",LocalDate.now().getYear());return "pages/admin/settings";}
 @PostMapping("/balances")public String balance(@RequestParam Long userId,@RequestParam int year,@RequestParam int days,HttpSession s){if(!isAdmin(s))return "redirect:/login";try{policy.allocate(users.findById(userId),year,days);activity.log((UserEntity)s.getAttribute("currentUser"),"ALLOCATE_BALANCE",userId,year+":"+days);}catch(RuntimeException e){return "redirect:/admin/settings?error=balance";}return "redirect:/admin/settings?success=balance";}
 @PostMapping("/holidays") public String holiday(@RequestParam LocalDate date,@RequestParam String name,HttpSession s){
  if(!isAdmin(s))return "redirect:/admin/login";
  try{
   if(policy.holidays().stream().anyMatch(h->h.getDate().equals(date)))return "redirect:/admin/calendar?error=duplicate";
   policy.addHoliday(date,name);
   activity.notifyEmployees("[Lịch nghỉ công ty] "+name.trim()+" - ngày "+date+". Xem lịch nghỉ để biết chi tiết.","/user/calendar");
   activity.log((UserEntity)s.getAttribute("currentUser"),"ADD_COMPANY_HOLIDAY",null,date+" - "+name.trim());
  }catch(RuntimeException e){return "redirect:/admin/calendar?error=holiday";}
  return "redirect:/admin/calendar?success=holiday";
 }
 @PostMapping("/holidays/delete/{id}")public String deleteHoliday(@PathVariable Long id,HttpSession s){
  if(!isAdmin(s))return "redirect:/admin/login";
  HolidayEntity h=policy.holidays().stream().filter(item->item.getId().equals(id)).findFirst().orElse(null);
  if(h==null)return "redirect:/admin/calendar?error=missing";
  policy.deleteHoliday(id);
  activity.notifyEmployees("[Lịch nghỉ công ty] Ngày nghỉ "+h.getDate()+" ("+h.getName()+") đã bị hủy/cập nhật.","/user/calendar");
  activity.log((UserEntity)s.getAttribute("currentUser"),"DELETE_COMPANY_HOLIDAY",id,h.getDate()+" - "+h.getName());
  return "redirect:/admin/calendar?success=holiday-delete";
 }
 @PostMapping("/departments")public String department(@RequestParam String name,HttpSession s){if(!isAdmin(s))return "redirect:/login";try{departments.add(name);}catch(RuntimeException e){return "redirect:/admin/settings?error=department";}return "redirect:/admin/settings?success=department";}
 @PostMapping("/departments/assign")public String assign(@RequestParam Long userId,@RequestParam Long departmentId,HttpSession s){if(!isAdmin(s))return "redirect:/login";try{departments.assign(userId,departmentId);}catch(RuntimeException e){return "redirect:/admin/settings?error=department";}return "redirect:/admin/settings?success=assign";}
 @PostMapping("/departments/manager")public String manager(@RequestParam Long departmentId,@RequestParam Long userId,HttpSession s){if(!isAdmin(s))return "redirect:/login";try{departments.manager(departmentId,userId);}catch(RuntimeException e){return "redirect:/admin/settings?error=manager";}return "redirect:/admin/settings?success=manager";}
 @GetMapping("/announcements") public String announcements(Model m,HttpSession s){
  if(!isAdmin(s))return "redirect:/admin/login";
  m.addAttribute("users",users.findAll().stream().filter(u->!"ADMIN".equals(u.getRole())).toList());
  m.addAttribute("sentLogs",activity.logs().stream().filter(log->"ADMIN_ANNOUNCEMENT".equals(log.getAction())).toList());
  return "pages/admin/announcements";
 }
 @PostMapping("/announcements/send") public String send(@RequestParam(required=false) Long recipientId,@RequestParam String message,HttpSession s){
  if(!isAdmin(s))return "redirect:/admin/login";
  try{int count=activity.sendFromAdmin((UserEntity)s.getAttribute("currentUser"),recipientId,message);
      return "redirect:/admin/announcements?sent="+count;
  }catch(RuntimeException e){return "redirect:/admin/announcements?error=invalid";}
 }
 @GetMapping("/audit")public String audits(Model m,HttpSession s){if(!isAdmin(s))return "redirect:/login";m.addAttribute("logs",activity.logs());return "pages/admin/audit";}
}
