package com.javaweb.service;

import com.javaweb.entity.*;
import com.javaweb.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository repository;
    private final LeavePolicyService policy;
    private final ActivityService activity;

    @Autowired
    public LeaveRequestService(LeaveRequestRepository repository, LeavePolicyService policy, ActivityService activity) {
        this.repository=repository;this.policy=policy;this.activity=activity;
    }
    // Constructor compatible with existing test double.
    public LeaveRequestService(LeaveRequestRepository repository){this(repository,null,null);}
    public List<LeaveRequestEntity> findAll(){return repository.findAll();}
    public List<LeaveRequestEntity> findByUser(UserEntity user){return repository.findByUser(user);}
    public LeaveRequestEntity findById(Long id){return repository.findById(id).orElse(null);}
    public long countAll(){return repository.count();}
    public long countPending(){return repository.countByStatus("PENDING")+repository.countByStatus("MANAGER_PENDING");}
    public long countApproved(){return repository.countByStatus("APPROVED");}
    public long countRejected(){return repository.countByStatus("REJECTED");}
    public long countByUser(UserEntity user){return repository.countByUser(user);}
    public long countByUserAndStatus(UserEntity user,String status){return repository.countByUserAndStatus(user,status);}
    public List<LeaveRequestEntity> findByStatus(String status){
        if("PENDING".equals(status))return repository.findByStatusIn(List.of("PENDING","MANAGER_PENDING"));
        return repository.findByStatus(status);
    }
    public List<LeaveRequestEntity> pendingForManager(UserEntity manager){
        if(manager==null || !"MANAGER".equals(manager.getRole()) || manager.getDepartment()==null)return List.of();
        if(manager.getDepartment().getManager()==null || !manager.getDepartment().getManager().getId().equals(manager.getId()))return List.of();
        return repository.findByUserDepartmentIdAndStatus(manager.getDepartment().getId(),"MANAGER_PENDING");
    }
    @Transactional
    public LeaveRequestEntity submit(UserEntity user,String reason,LocalDate start,LocalDate end,Long typeId){
        if(user==null||user.getId()==null||"ADMIN".equals(user.getRole()))throw new IllegalArgumentException("Tài khoản không được tạo đơn");
        if(reason==null||reason.trim().length()<5||reason.length()>3000)throw new IllegalArgumentException("Lý do cần từ 5 đến 3000 ký tự");
        if(start==null||end==null||end.isBefore(start)||start.isBefore(LocalDate.now()))throw new IllegalArgumentException("Ngày nghỉ phải hợp lệ và không thuộc quá khứ");
        if(start.getYear()!=end.getYear())throw new IllegalArgumentException("Hãy tách đơn nghỉ qua năm thành hai đơn riêng");
        int days=policy.workingDays(start,end);
        if(days==0)throw new IllegalArgumentException("Khoảng nghỉ không chứa ngày làm việc");
        LeaveTypeEntity type=policy.type(typeId);
        for(LeaveRequestEntity existing:repository.findByUserIdAndStatusIn(user.getId(),List.of("PENDING","MANAGER_PENDING","APPROVED"))){
            if(existing.getStartDate()!=null&&existing.getEndDate()!=null&&
               !start.isAfter(existing.getEndDate())&&!end.isBefore(existing.getStartDate()))
                throw new IllegalArgumentException("Đơn nghỉ bị trùng lịch với đơn #"+existing.getId());
        }
        if(type.isDeductBalance()&&policy.remaining(user,start.getYear())<days)
           throw new IllegalArgumentException("Số ngày phép năm còn lại không đủ");
        LeaveRequestEntity l=new LeaveRequestEntity();l.setUser(user);l.setReason(reason.trim());l.setStartDate(start);l.setEndDate(end);
        l.setWorkDays(days);l.setLeaveType(type);l.setCreatedAt(LocalDateTime.now());
        boolean managerApproval=user.getDepartment()!=null&&user.getDepartment().getManager()!=null
           && !user.getDepartment().getManager().getId().equals(user.getId());
        l.setStatus(managerApproval?"MANAGER_PENDING":"PENDING");
        repository.save(l);
        if(managerApproval)activity.notifyUser(user.getDepartment().getManager(),"Đơn #"+l.getId()+" đang chờ trưởng phòng duyệt","/manager/leaves");
        else activity.notifyAdmins("Có đơn nghỉ phép #"+l.getId()+" mới","/admin/leaves");
        activity.log(user,"CREATE_LEAVE",l.getId(),"Đăng ký "+days+" ngày làm việc");
        return l;
    }
    // Compatibility method used by older code; route all requests through submit validation.
    public LeaveRequestEntity createLeaveRequest(LeaveRequestEntity leave,UserEntity user){return submit(user,leave.getReason(),leave.getStartDate(),leave.getEndDate(),leave.getLeaveType()==null?null:leave.getLeaveType().getId());}
    @Transactional
    public void approve(Long id,UserEntity admin){
        LeaveRequestEntity l=requirePending(id,"PENDING");
        l.setStatus("APPROVED");l.setAdminNote("Đơn đã được duyệt");l.setReviewedBy(admin);l.setReviewedAt(LocalDateTime.now());repository.save(l);
        activity.notifyUser(l.getUser(),"Đơn #"+id+" đã được duyệt","/leave/detail/"+id);
        activity.log(admin,"APPROVE",id,"Duyệt đơn nghỉ phép");
    }
    @Transactional
    public void reject(Long id,String note,UserEntity admin){
        LeaveRequestEntity l=requirePending(id,"PENDING");
        l.setStatus("REJECTED");l.setAdminNote(note==null||note.isBlank()?"Không được duyệt":note.trim());l.setReviewedBy(admin);l.setReviewedAt(LocalDateTime.now());repository.save(l);
        activity.notifyUser(l.getUser(),"Đơn #"+id+" bị từ chối","/leave/detail/"+id);
        activity.log(admin,"REJECT",id,l.getAdminNote());
    }
    @Transactional public void managerDecision(Long id,UserEntity manager,boolean approve,String note){
        LeaveRequestEntity l=requirePending(id,"MANAGER_PENDING");
        if(manager==null||!"MANAGER".equals(manager.getRole())||manager.getDepartment()==null||l.getUser().getDepartment()==null
         ||!manager.getDepartment().getId().equals(l.getUser().getDepartment().getId())
         ||manager.getDepartment().getManager()==null||!manager.getDepartment().getManager().getId().equals(manager.getId()))
            throw new SecurityException("Bạn không có quyền xử lý đơn này");
        l.setReviewedBy(manager);l.setReviewedAt(LocalDateTime.now());
        if(approve){l.setStatus("PENDING");l.setAdminNote("Trưởng phòng đã đồng ý – chờ Admin duyệt");activity.notifyAdmins("Trưởng phòng đã chuyển đơn #"+id,"/admin/leaves");}
        else {l.setStatus("REJECTED");l.setAdminNote(note==null||note.isBlank()?"Trưởng phòng từ chối":note.trim());}
        repository.save(l);activity.notifyUser(l.getUser(),"Đơn #"+id+(approve?" đã qua vòng trưởng phòng":" bị trưởng phòng từ chối"),"/leave/detail/"+id);
        activity.log(manager,approve?"MANAGER_APPROVE":"MANAGER_REJECT",id,l.getAdminNote());
    }
    private LeaveRequestEntity requirePending(Long id,String status){LeaveRequestEntity l=findById(id);if(l==null||!status.equals(l.getStatus()))throw new IllegalArgumentException("Đơn không tồn tại hoặc đã được xử lý");return l;}
    @Transactional
    public void cancelLeaveRequest(Long id,UserEntity user){
        LeaveRequestEntity l=findById(id);
        if(l==null||user==null||!l.getUser().getId().equals(user.getId())||!("PENDING".equals(l.getStatus())||"MANAGER_PENDING".equals(l.getStatus())))throw new IllegalArgumentException("Không thể hủy đơn này");
        l.setStatus("CANCELLED");repository.save(l);activity.log(user,"CANCEL",id,"Nhân viên hủy đơn");
    }
}
