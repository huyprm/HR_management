package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.JobGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobGradeRepository extends JpaRepository<JobGrade, String> {
    boolean existsByName(String name);
}
