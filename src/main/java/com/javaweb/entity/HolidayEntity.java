package com.javaweb.entity;
import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name="holidays")
public class HolidayEntity {
@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
@Column(name="holiday_date",nullable=false,unique=true) private LocalDate date;
@Column(nullable=false,length=120) private String name;
    public Long getId(){return id;}
    public void setId(Long value){this.id=value;}
    public LocalDate getDate(){return date;}
    public void setDate(LocalDate value){this.date=value;}
    public String getName(){return name;}
    public void setName(String value){this.name=value;}
}
