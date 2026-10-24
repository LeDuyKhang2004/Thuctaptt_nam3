package com.javaweb.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="leave_types")
public class LeaveTypeEntity {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
@Column(nullable=false,unique=true,length=30) private String code;
@Column(nullable=false,length=120) private String name;
@Column(nullable=false) private boolean deductBalance;
    public Long getId(){return id;}
    public void setId(Long value){this.id=value;}
    public String getCode(){return code;}
    public void setCode(String value){this.code=value;}
    public String getName(){return name;}
    public void setName(String value){this.name=value;}
    public boolean getDeductBalance(){return deductBalance;}
    public void setDeductBalance(boolean value){this.deductBalance=value;}
    public boolean isDeductBalance(){return deductBalance;}
}
