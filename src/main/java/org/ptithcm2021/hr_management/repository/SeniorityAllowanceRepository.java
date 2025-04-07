package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.SeniorityAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeniorityAllowanceRepository extends JpaRepository<SeniorityAllowance, Integer> {
}
