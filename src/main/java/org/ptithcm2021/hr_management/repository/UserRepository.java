package org.ptithcm2021.hr_management.repository;

import jakarta.validation.constraints.Email;
import org.ptithcm2021.hr_management.controller.UserController;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.User;
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
}
