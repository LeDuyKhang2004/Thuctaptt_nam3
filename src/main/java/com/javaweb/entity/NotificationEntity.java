package com.javaweb.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="notifications")
public class NotificationEntity {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
@ManyToOne(optional=false) @JoinColumn(name="user_id") private UserEntity user;
@Column(nullable=false,length=250) private String message;
@Column(name="target_url",length=255) private String targetUrl;
@Column(name="is_read",nullable=false) private boolean read;
@Column(name="created_at",nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId(){return id;}
    public void setId(Long value){this.id=value;}
    public UserEntity getUser(){return user;}
    public void setUser(UserEntity value){this.user=value;}
    public String getMessage(){return message;}
    public void setMessage(String value){this.message=value;}
    public String getTargetUrl(){return targetUrl;}
    public void setTargetUrl(String value){this.targetUrl=value;}
    public boolean getRead(){return read;}
    public void setRead(boolean value){this.read=value;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime value){this.createdAt=value;}
    public boolean isRead(){return read;}
}
