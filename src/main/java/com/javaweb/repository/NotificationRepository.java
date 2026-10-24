package com.javaweb.repository;
import com.javaweb.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {
 java.util.List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
 long countByUserIdAndReadFalse(Long userId);
 int deleteByUserIdAndReadTrue(Long userId);
}
