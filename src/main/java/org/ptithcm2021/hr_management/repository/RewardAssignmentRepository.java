package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.RewardAssignmentId;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardAssignmentRepository extends JpaRepository<RewardAssignment, RewardAssignmentId> {
}
