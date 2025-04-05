package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
    Optional<LeaveBalance> findByUserIdAndYear(long userId, int year);

    Optional<List<LeaveBalance>> findAllByYear(int year);
}
