package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.RewardAssignmentId;
import org.ptithcm2021.hr_management.model.RewardAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardAssignmentRepository extends JpaRepository<RewardAssignment, RewardAssignmentId> {
    @Modifying
    @Query(value = "update rewardAssignments ra " +
            "set ra.id.userId = :newUserId " +
            "where ra.id.userId = :userId and ra.id.rewardDecisionId = :rewardDecisionId")
    int updateUser(@Param("rewardDecisionId") String rewardDecisionId,
                             @Param("userId") long usrId,
                             @Param("newUserId") long userId);

    @Modifying
    @Query(value = "update rewardAssignments ra " +
            "set ra.id.rewardDecisionId = :newRewardDecisionId " +
            "where ra.id.userId = :userId and ra.id.rewardDecisionId = :rewardDecisionId")
    int updateRewardDecision(@Param("rewardDecisionId") String rewardDecisionId,
                             @Param("userId") long usrId,
                             @Param("newRewardDecision") String newRewardDecision);
}
