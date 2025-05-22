package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.DecisionEnum;
import org.ptithcm2021.hr_management.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DecisionRepository extends JpaRepository<Decision, String> {
    List<Decision> findAllByType(DecisionEnum decisionType);
    
    List<Decision> findByProcessedFalseAndEffectiveDateLessThanEqual(LocalDate currentDate);
    
    List<Decision> findByTypeAndProcessedFalseAndEffectiveDateLessThanEqual(
            DecisionEnum type, LocalDate currentDate);

    @Query("select d from Decision d where d.user.id= :userId and d.signer.id is not null")
    List<Decision> findAllByUserId(@Param("userId") long userId);

    @Query("select d from Decision d where d.user.id= :userId and d.signer is not null and d.type= :decisionType")
    List<Decision> findAllByUserIdAndType(@Param("userId") long userId,
                                          @Param("decisionType") DecisionEnum decisionType);
}
