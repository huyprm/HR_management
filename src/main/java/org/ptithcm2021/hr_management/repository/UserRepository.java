package org.ptithcm2021.hr_management.repository;

import jakarta.validation.constraints.Email;
import org.ptithcm2021.hr_management.dto.response.UserSummaryResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.projection.UserSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(@Email(message = "Email invalid") String email);

    @Query("SELECT u FROM User u WHERE u.status = org.ptithcm2021.hr_management.enums.UserStatusEnum.PENDING")
    List<User> findAllActiveUsers();

    Page<User> findAllByStatus(UserStatusEnum status, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.position.role.id = :role AND u.status = 'ACTIVE' ")
    Page<User> findAllUserByRole(@Param("role")RoleEnum role, Pageable pageable);

    @Query("SELECT u FROM User u JOIN Contract c ON u.id = c.user.id WHERE c.contractStatusEnum = :status")
    Page<User> findAllUserByContract(@Param("status") ContractStatusEnum status, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN Contract c ON u.id = c.user.id WHERE c.id IS NULL or c.contractStatusEnum = 'TERMINATED'")
    Page<User> findUsersWithoutContract(Pageable pageable);

    @Query("select u from User u where u.position.department.id = :departmentId and u.status= :status")
    Page<User> findByDepartmentIdAndStatus(@Param("departmentId") String departmentId,
                                     @Param("status") UserStatusEnum status, Pageable pageable);

    @Query("select u from User u where u.position.department.id = :departmentId")
    Page<User> findAllByDepartmentId(@Param("departmentId") String departmentId,
                                     Pageable pageable);

    @Query("select distinct c.user from Contract c where (c.contractStatusEnum = :status " +
            "or ((c.endDate BETWEEN :startOfMonth AND :endOfMonth) and c.contractStatusEnum in ('EXPIRED','RENEWED')))")
    List<User> findActiveOrRecentlyEndedContractUsers(@Param("startOfMonth") LocalDate startOfMonth,
                                                      @Param("endOfMonth") LocalDate endOfMonth,
                                                      @Param("status") ContractStatusEnum status);

    @Query(value = "SELECT u.id AS id, u.full_name AS fullName FROM users u WHERE MATCH(u.full_name) AGAINST (?1 IN BOOLEAN MODE) AND u.status = 'ACTIVE'", nativeQuery = true)
    List<UserSummary> searchFullText(String keyword);

    @Query("select u from User u where u.position.department.id = :departmentId and u.status= :status")
    List<User> findAllByDepartmentId(@Param("departmentId") String departmentId,
                                     @Param("status") UserStatusEnum status);

    User findUserById(long id);
}
