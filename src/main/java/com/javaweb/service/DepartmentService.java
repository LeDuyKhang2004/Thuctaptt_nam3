package com.javaweb.service;
import com.javaweb.repository.*;
import com.javaweb.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class DepartmentService {
 private final DepartmentRepository departments; private final UserRepository users;
 public DepartmentService(DepartmentRepository d,UserRepository u){departments=d;users=u;}
 public List<DepartmentEntity> all(){return departments.findAll();}
 @Transactional public void add(String name){if(name==null||name.isBlank())throw new IllegalArgumentException("Tên phòng ban trống");DepartmentEntity d=new DepartmentEntity();d.setName(name.trim());departments.save(d);}
 @Transactional public void assign(Long userId,Long depId){UserEntity user=users.findById(userId).orElseThrow();user.setDepartment(depId==null?null:departments.findById(depId).orElseThrow());users.save(user);}
 @Transactional public void manager(Long depId,Long managerId){DepartmentEntity dep=departments.findById(depId).orElseThrow();UserEntity manager=users.findById(managerId).orElseThrow();manager.setRole("MANAGER");manager.setDepartment(dep);users.save(manager);dep.setManager(manager);departments.save(dep);}
}
