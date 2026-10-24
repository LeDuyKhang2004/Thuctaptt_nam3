package com.javaweb.repository;
import com.javaweb.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity,Long> {
    LeaveTypeEntity findByCode(String code);
}
