package com.javaweb.service;

import com.javaweb.entity.*;
import com.javaweb.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class ActivityService {
    private final NotificationRepository notifications;
    private final AuditLogRepository audits;
    private final UserRepository users;
    private final OptionalEmailService mail;
    public ActivityService(NotificationRepository n, AuditLogRepository a, UserRepository u, OptionalEmailService mail) {
        this.notifications=n; this.audits=a; this.users=u; this.mail=mail;
    }
    public void notifyUser(UserEntity user,String message,String path) {
        if(user == null) return;
        NotificationEntity n = new NotificationEntity();
        n.setUser(user); n.setMessage(message); n.setTargetUrl(path);
        notifications.save(n);
        mail.send(user.getEmail(),"Thông báo LeaveFlow",message);
    }
    public void notifyAdmins(String msg,String path) {
        for(UserEntity u:users.findAll()) if("ADMIN".equals(u.getRole())) notifyUser(u,msg,path);
    }
    @Transactional
    public int notifyEmployees(String message, String path) {
        int count=0;
        for(UserEntity u : users.findAll()) {
            if(!"ADMIN".equals(u.getRole())) { notifyUser(u,message,path); count++; }
        }
        return count;
    }
    @Transactional
    public int sendFromAdmin(UserEntity admin, Long recipientId, String message) {
        if(admin==null || !"ADMIN".equals(admin.getRole())) throw new SecurityException("Không có quyền gửi thông báo");
        if(message==null || message.isBlank() || message.trim().length()>500)
            throw new IllegalArgumentException("Thông báo phải có từ 1 đến 500 ký tự");
        String content="[Thông báo từ Admin] " + message.trim();
        int count;
        if(recipientId==null) {
            count=notifyEmployees(content,"");
        } else {
            UserEntity target=users.findById(recipientId).orElseThrow(()->new IllegalArgumentException("Không tìm thấy nhân viên"));
            if("ADMIN".equals(target.getRole())) throw new IllegalArgumentException("Chỉ được gửi tới nhân viên hoặc trưởng phòng");
            notifyUser(target,content,""); count=1;
        }
        log(admin,"ADMIN_ANNOUNCEMENT",recipientId,"Đã gửi cho "+count+" người: "+message.trim());
        return count;
    }
    public void log(UserEntity actor,String action,Long entityId,String detail){
        AuditLogEntity a=new AuditLogEntity();
        a.setActorId(actor==null?null:actor.getId());a.setAction(action);a.setEntityId(entityId);a.setDetail(detail);audits.save(a);
    }
    public List<NotificationEntity> forUser(UserEntity user) {return notifications.findByUserIdOrderByCreatedAtDesc(user.getId());}
    public long unread(UserEntity user) {return notifications.countByUserIdAndReadFalse(user.getId());}
    @Transactional
    public void markRead(Long id,UserEntity user) {
        notifications.findById(id).ifPresent(n->{
            if(n.getUser().getId().equals(user.getId())) {n.setRead(true);notifications.save(n);}
        });
    }
    @Transactional
    public boolean deleteIfRead(Long id,UserEntity user) {
        NotificationEntity n=notifications.findById(id).orElse(null);
        if(n==null || user==null || !n.getUser().getId().equals(user.getId()) || !n.getRead()) return false;
        notifications.delete(n);return true;
    }
    @Transactional
    public int deleteAllRead(UserEntity user) {
        return notifications.deleteByUserIdAndReadTrue(user.getId());
    }
    public List<AuditLogEntity> logs(){return audits.findTop100ByOrderByCreatedAtDesc();}
}
