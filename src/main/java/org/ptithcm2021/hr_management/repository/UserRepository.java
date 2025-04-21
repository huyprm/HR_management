package org.ptithcm2021.hr_management.repository;

import jakarta.validation.constraints.Email;
import org.ptithcm2021.hr_management.controller.UserController;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(@Email(message = "Email invalid") String email);

    @Query("SELECT u FROM User u WHERE u.status = org.ptithcm2021.hr_management.enums.UserStatusEnum.PENDING")
    List<User> findAllActiveUsers();

    Page<User> findAllByStatus(UserStatusEnum status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.position.role = :role")
    Page<User> findAllUserByRole(@Param("role")RoleEnum role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN Contract c ON u.id = c.user.id WHERE c.contractStatusEnum = :status")
    Page<User> findAllUserByContract(@Param("status") ContractStatusEnum status, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN Contract c ON u.id = c.user.id WHERE c.id IS NULL ")
    Page<User> findUsersWithoutContract(Pageable pageable);
}
