package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    List<Contract> findContractByUserId (long userId);

    List<Contract> findContractByContractStatusEnum(ContractStatusEnum contractStatusEnum);

    Optional<Contract> findContractByUserIdAndContractStatusEnum(long userId, ContractStatusEnum contractStatusEnum);
}
