package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.dto.response.DepartmentResponse;
import org.ptithcm2021.hr_management.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    boolean existsByName(String name);

    @Query("select d from Department d join User u on u.position.department.id = d.id where u.id = :userId")
    Optional<Department> findByUserId(@Param("userId") long userId);
}
