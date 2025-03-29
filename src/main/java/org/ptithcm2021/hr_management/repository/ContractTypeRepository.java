package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Integer> {
    boolean existsByName(String name);
}
