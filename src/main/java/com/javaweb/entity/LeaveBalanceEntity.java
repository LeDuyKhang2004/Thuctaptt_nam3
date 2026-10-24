package com.javaweb.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="leave_balances", uniqueConstraints=@UniqueConstraint(columnNames={"user_id","leave_year"}))
public class LeaveBalanceEntity {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
@ManyToOne(optional=false) @JoinColumn(name="user_id") private UserEntity user;
@Column(name="leave_year",nullable=false) private Integer year;
@Column(nullable=false) private Integer allocatedDays = 12;
    public Long getId(){return id;}
    public void setId(Long value){this.id=value;}
    public UserEntity getUser(){return user;}
    public void setUser(UserEntity value){this.user=value;}
    public Integer getYear(){return year;}
    public void setYear(Integer value){this.year=value;}
    public Integer getAllocatedDays(){return allocatedDays;}
    public void setAllocatedDays(Integer value){this.allocatedDays=value;}
}
