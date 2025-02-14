package com.weavus.weavusys.workSchedule.repo;

import com.weavus.weavusys.workSchedule.entity.EmployeeWorkDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeWorkDateRepo extends JpaRepository<EmployeeWorkDate, Long > {

    boolean existsByEmployeeId(String id);

    EmployeeWorkDate findByEmployeeId(String id);
}
