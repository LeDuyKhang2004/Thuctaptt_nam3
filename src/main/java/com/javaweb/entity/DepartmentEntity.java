package com.javaweb.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="departments")
public class DepartmentEntity {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
@Column(nullable=false,unique=true,length=120) private String name;
@ManyToOne @JoinColumn(name="manager_id") private UserEntity manager;
    public Long getId(){return id;}
    public void setId(Long value){this.id=value;}
    public String getName(){return name;}
    public void setName(String value){this.name=value;}
    public UserEntity getManager(){return manager;}
    public void setManager(UserEntity value){this.manager=value;}
}
