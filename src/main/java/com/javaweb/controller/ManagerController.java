package com.javaweb.controller;
import com.javaweb.entity.*;import com.javaweb.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/manager")
public class ManagerController {
 private final LeaveRequestService leaves;
 public ManagerController(LeaveRequestService leaves){this.leaves=leaves;}
 @GetMapping("/leaves") public String list(HttpSession s,Model m){UserEntity u=(UserEntity)s.getAttribute("currentUser");if(u==null||!"MANAGER".equals(u.getRole()))return "redirect:/login";m.addAttribute("leaves",leaves.pendingForManager(u));return "pages/manager/leaves";}
 @PostMapping("/decision/{id}") public String decision(@PathVariable Long id,@RequestParam String decision,@RequestParam(required=false) String note,HttpSession s){UserEntity u=(UserEntity)s.getAttribute("currentUser");if(u==null||!"MANAGER".equals(u.getRole()))return "redirect:/login";try{leaves.managerDecision(id,u,"approve".equals(decision),note);}catch(RuntimeException e){return "redirect:/manager/leaves?error=1";}return "redirect:/manager/leaves?success=1";}
}
