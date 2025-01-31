package com.weavus.weavusys.workSchedule.repo;

import com.weavus.weavusys.workSchedule.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkScheduleRepo extends JpaRepository<WorkSchedule, LocalDate> {
    @Query("SELECT a FROM WorkSchedule a WHERE a.employee.id = :employeeId " +
            "AND YEAR(a.workDate) = :year " +
            "AND MONTH(a.workDate) = :month")
    List<WorkSchedule> findByEmployeeAndWorkDateBetween(@Param("employeeId") Long employeeId,
                                                        @Param("year") int year,
                                                        @Param("month") int month);

}
