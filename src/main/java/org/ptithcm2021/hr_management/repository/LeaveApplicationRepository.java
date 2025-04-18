package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> getAllByFormStatusEnum(FormStatusEnum formStatusEnum);
    List<LeaveApplication> findAllByUserId(long userId);
    
    // Phương thức mới để tìm kiếm đơn nghỉ phép đã duyệt trong một tháng cụ thể
    @Query("SELECT la FROM LeaveApplication la WHERE la.user.id = :userId " +
           "AND la.formStatusEnum = 'APPROVED' " +
           "AND (YEAR(la.startDate) = :year AND MONTH(la.startDate) = :month " +
           "OR YEAR(la.endDate) = :year AND MONTH(la.endDate) = :month " +
           "OR (la.startDate <= :endOfMonth AND la.endDate >= :startOfMonth))")
    List<LeaveApplication> findApprovedLeavesByUserAndMonth(
            @Param("userId") Long userId, 
            @Param("year") int year, 
            @Param("month") int month,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth);

    @Query("SELECT la FROM LeaveApplication la WHERE " +
            "la.formStatusEnum = 'APPROVED' " +
            "AND (YEAR(la.startDate) = :year AND MONTH(la.startDate) = :month " +
            "OR YEAR(la.endDate) = :year AND MONTH(la.endDate) = :month " +
            "OR (la.startDate <= :endOfMonth AND la.endDate >= :startOfMonth))")
    List<LeaveApplication> findApprovedLeavesByMonth(
            @Param("year") int year,
            @Param("month") int month,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth);
}
