package com.weavus.weavusys.workSchedule.repo;

import com.weavus.weavusys.workSchedule.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkScheduleRepo extends JpaRepository<WorkSchedule, Long> {
    @Query("SELECT a FROM WorkSchedule a WHERE a.employee.id = :employeeId " +
            "AND YEAR(a.checkInDate) = :year " +
            "AND MONTH(a.checkInDate) = :month")
    List<WorkSchedule> findByEmployeeAndWorkDateBetween(@Param("employeeId") String employeeId,
                                                        @Param("year") int year,
                                                        @Param("month") int month);

    boolean existsByEmployeeIdAndCheckInDate(String id, LocalDate checkInDate);
}
