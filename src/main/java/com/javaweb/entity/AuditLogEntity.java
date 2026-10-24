package com.javaweb.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="audit_logs")
public class AuditLogEntity {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
@Column(name="actor_id") private Long actorId;
@Column(nullable=false,length=80) private String action;
@Column(name="entity_id") private Long entityId;
@Column(columnDefinition="TEXT") private String detail;
@Column(name="created_at",nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId(){return id;}
    public void setId(Long value){this.id=value;}
    public Long getActorId(){return actorId;}
    public void setActorId(Long value){this.actorId=value;}
    public String getAction(){return action;}
    public void setAction(String value){this.action=value;}
    public Long getEntityId(){return entityId;}
    public void setEntityId(Long value){this.entityId=value;}
    public String getDetail(){return detail;}
    public void setDetail(String value){this.detail=value;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime value){this.createdAt=value;}
}
