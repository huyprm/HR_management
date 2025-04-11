package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> getAllByFormStatusEnum(FormStatusEnum formStatusEnum);
    List<LeaveApplication> findAllByUserId(long userId);
}
