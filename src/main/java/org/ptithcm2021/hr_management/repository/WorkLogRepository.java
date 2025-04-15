package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.WorkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkingHistory, Integer> {
    @Query("SELECT w.type, COUNT(w) FROM WorkingHistory w WHERE w.user.id = :userId AND (w.type = 'AWARD' OR w.type = 'DISCIPLINE') GROUP BY w.type")
    List<Object[]> countRewardAndDisciplineByUserId(@Param("userId") long userId);

    List<WorkingHistory> findAllByUserId(long userId);

}
