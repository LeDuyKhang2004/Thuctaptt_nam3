package com.javaweb.controller;
import com.javaweb.entity.*;
import com.javaweb.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class LeaveRequestController {
 private final LeaveRequestService leaves;private final LeavePolicyService policy;
 public LeaveRequestController(LeaveRequestService l,LeavePolicyService p){leaves=l;policy=p;}
 @GetMapping("/leave/create") public String form(HttpSession session,Model model){UserEntity u=(UserEntity)session.getAttribute("currentUser");if(u==null)return "redirect:/login";model.addAttribute("types",policy.types());model.addAttribute("balance",policy.remaining(u,LocalDate.now().getYear()));return "pages/users/leave-create";}
 @PostMapping("/leave/create") public String create(@RequestParam String reason,@RequestParam LocalDate startDate,@RequestParam LocalDate endDate,@RequestParam Long typeId,HttpSession session,Model model){
  UserEntity user=(UserEntity)session.getAttribute("currentUser");if(user==null)return "redirect:/login";
  try{leaves.submit(user,reason,startDate,endDate,typeId);return "redirect:/leave/my?success=create";}
  catch(IllegalArgumentException e){model.addAttribute("error",e.getMessage());model.addAttribute("types",policy.types());model.addAttribute("balance",policy.remaining(user,LocalDate.now().getYear()));return "pages/users/leave-create";}
 }
 @GetMapping("/leave/my") public String my(Model model,HttpSession s){UserEntity u=(UserEntity)s.getAttribute("currentUser");if(u==null)return "redirect:/login";model.addAttribute("leaves",leaves.findByUser(u));return "pages/users/my-leaves";}
 @PostMapping("/leave/cancel/{id}") public String cancel(@PathVariable Long id,HttpSession s){UserEntity u=(UserEntity)s.getAttribute("currentUser");if(u==null)return "redirect:/login";try{leaves.cancelLeaveRequest(id,u);}catch(IllegalArgumentException ignored){return "redirect:/leave/my?error=cancel";}return "redirect:/leave/my?success=cancel";}
 @GetMapping("/leave/detail/{id}") public String detail(@PathVariable Long id,Model model,HttpSession s){UserEntity u=(UserEntity)s.getAttribute("currentUser");if(u==null)return "redirect:/login";LeaveRequestEntity l=leaves.findById(id);if(l==null||!l.getUser().getId().equals(u.getId()))return "redirect:/leave/my";model.addAttribute("leave",l);return "pages/users/leave-detail";}
}
