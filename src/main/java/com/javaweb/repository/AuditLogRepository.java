package com.javaweb.repository;
import com.javaweb.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditLogRepository extends JpaRepository<AuditLogEntity,Long> {
    java.util.List<AuditLogEntity> findTop100ByOrderByCreatedAtDesc();
}
