package com.javaweb.repository;
import com.javaweb.entity.LeaveBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalanceEntity,Long> {
    LeaveBalanceEntity findByUserIdAndYear(Long userId, Integer year);
}
