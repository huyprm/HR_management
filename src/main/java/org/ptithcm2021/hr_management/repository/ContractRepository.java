package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Contract> findAllContractByContractStatusEnum(ContractStatusEnum contractStatusEnum, Pageable pageable);

    @Query("SELECT c FROM Contract c WHERE c.user.id = :userId "
            + "AND (:status IS NULL OR c.contractStatusEnum = :status)")
    Page<Contract> findContractByUserIdAndContractStatusEnum(@Param("userId") long userId,
                                                 @Param("status") ContractStatusEnum status,
                                                 Pageable pageable);

    Optional<Contract> findContractByUserIdAndContractStatusEnum(long userId, ContractStatusEnum contractStatusEnum);

    List<Contract> findAllByContractStatusEnum(ContractStatusEnum statusEnum);

    @Query("select c from Contract c where c.endDate < :date and c.contractStatusEnum = 'ACTIVE'")
    List<Contract> findAllContractExpiry(@Param("date") LocalDate date);

    @Query("select c from Contract  c where c.contractStatusEnum != 'ACTIVE' and c.user.id = :userId")
    List<Contract> findAllContractByUserIdIsNotActive (@Param("userId")long userId);
}
