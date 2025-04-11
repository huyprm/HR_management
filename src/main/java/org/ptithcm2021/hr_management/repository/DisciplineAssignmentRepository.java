package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.AssignmentId;
import org.ptithcm2021.hr_management.model.DisciplineAssignment;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisciplineAssignmentRepository extends JpaRepository<DisciplineAssignment, AssignmentId> {
    Optional<List<DisciplineAssignment>> findAllByUserId(long userId);
    int countByUserId(long userId);
}
