package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DecisionRepository extends JpaRepository<Decision, String> {
    List<Decision> findAllByType(DecisionEnum decisionType);
    
    List<Decision> findByProcessedFalseAndEffectiveDateLessThanEqual(Date currentDate);
    
    List<Decision> findByTypeAndProcessedFalseAndEffectiveDateLessThanEqual(
            DecisionEnum type, Date currentDate);

    List<Decision> findAllByUserId(long userId);

    List<Decision> findAllByUserIdAndType(long userId, DecisionEnum decisionType);
}
