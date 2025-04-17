package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findContractByUserId (long userId);

    List<Contract> findContractByContractStatusEnum(ContractStatusEnum contractStatusEnum);

    Optional<Contract> findContractByUserIdAndContractStatusEnum(long userId, ContractStatusEnum contractStatusEnum);

    @Query("SELECT c FROM Contract c WHERE c.user.id = :userId AND c.contractStatusEnum = org.ptithcm2021.hr_management.enums.ContractStatusEnum.ACTIVE ORDER BY c.endDate DESC")
    Optional<Contract> findCurrentContractByUserId(@Param("userId") long userId);

    List<Contract> findAllByContractStatusEnum(ContractStatusEnum statusEnum);
}
