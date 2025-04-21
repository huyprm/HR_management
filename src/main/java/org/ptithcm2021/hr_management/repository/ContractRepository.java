package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findContractByUserId (long userId);

    List<Contract> findContractByContractStatusEnum(ContractStatusEnum contractStatusEnum);

    Optional<Contract> findContractByUserIdAndContractStatusEnum(long userId, ContractStatusEnum contractStatusEnum);

    List<Contract> findAllByContractStatusEnum(ContractStatusEnum statusEnum);

    @Query("select c from Contract c where c.contractStatusEnum = 'ACTIVE' or (c.endDate >= :startOfMonth and c.endDate <= :endOfMonth)")
    List<Contract> findActiveOrRecentlyEndedContractUsers(@Param("startOfMonth") LocalDate startOfMonth,
                                                          @Param("endOfMonth") LocalDate endOfMonth);
}
