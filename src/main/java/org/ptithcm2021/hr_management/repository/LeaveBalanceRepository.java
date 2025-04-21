package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
    // Tìm kiếm theo userId, năm và tháng
    Optional<LeaveBalance> findByUserIdAndYearAndMonth(long userId, int year, int month);
    
    // Tìm tất cả bản ghi trong một tháng và năm cụ thể
    Optional<List<LeaveBalance>> findAllByYearAndMonth(int year, int month);
    
    // Tìm tất cả bản ghi của một người dùng trong một năm
    List<LeaveBalance> findAllByUserIdAndYear(long userId, int year);

}
