package com.javaweb.repository;
import com.javaweb.entity.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface HolidayRepository extends JpaRepository<HolidayEntity,Long> {
    boolean existsByDate(java.time.LocalDate date);
}
