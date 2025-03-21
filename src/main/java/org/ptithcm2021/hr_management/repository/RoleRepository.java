package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleEnum> {
}
