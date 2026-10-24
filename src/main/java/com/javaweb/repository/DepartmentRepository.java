package com.javaweb.repository;
import com.javaweb.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DepartmentRepository extends JpaRepository<DepartmentEntity,Long> {
}
