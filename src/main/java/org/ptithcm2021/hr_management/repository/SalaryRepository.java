//package org.ptithcm2021.hr_management.repository;
//
//import org.ptithcm2021.hr_management.model.Salary;
//import org.ptithcm2021.hr_management.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface SalaryRepository extends JpaRepository<Salary, Integer> {
//    List<Salary> findAllByUserId(long userId);
//
//    @Query("SELECT s FROM Salary s WHERE s.user = :user AND s.startDate <= :endDate AND s.endDate >= :startDate")
//    List<Salary> findAllBySalaryMonth(YearMonth yearMonth);
//
//    Optional<Salary> findByUserIdAndSalaryMonth(long userId, YearMonth yearMonth);
//
//    @Query(" SELECT COUNT(s) > 0 FROM Salary s WHERE s.user.id = :userId AND s.startDate <= :endDate AND s.endDate >= :startDate")
//    boolean existsByUserIdAndStartDateAndEndDate(@Param("userId") Long userId,
//                                @Param("startDate") LocalDate startDate,
//                                @Param("endDate") LocalDate endDate);
//
//
//}