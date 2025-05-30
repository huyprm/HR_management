package org.ptithcm2021.hr_management.projection;

import java.time.LocalDate;

public interface ContractExpireProjection {
    Integer getStt();
    String getFullName();
    String getEmail();
    String getDepartmentName();
    String getPositionName();
    String getContractTypeName();
    LocalDate getEndDate();
    Integer getRemainingDays();
    String getContractStatus();
}
