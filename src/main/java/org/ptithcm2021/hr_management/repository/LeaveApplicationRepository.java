package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> getAllByFormStatusEnum(FormStatusEnum formStatusEnum);
    List<LeaveApplication> findAllByUserId(long userId);


    @Query("SELECT la FROM LeaveApplication la " +
            "WHERE la.user.id = :userId " +
            "AND la.formStatusEnum = 'APPROVED' " +
            "AND la.startDate <= :endDate " +
            "AND la.endDate >= :startDate")
    List<LeaveApplication> findApprovedLeavesByUserAndMonth(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query("SELECT la FROM LeaveApplication la " +
            "WHERE la.formStatusEnum = 'APPROVED' " +
            "AND la.startDate <= :endDate " +
            "AND la.endDate >= :startDate")
    List<LeaveApplication> findApprovedLeavesByMonth(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth);
}
