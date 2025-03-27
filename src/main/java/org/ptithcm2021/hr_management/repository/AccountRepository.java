package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}
