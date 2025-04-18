package org.ptithcm2021.hr_management.config;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.LeaveBalance;
import org.ptithcm2021.hr_management.model.LeaveType;
import org.ptithcm2021.hr_management.model.Role;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.repository.LeaveTypeRepository;
import org.ptithcm2021.hr_management.repository.RoleRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Bean
    ApplicationRunner init(){
        return args -> {
            // Khởi tạo dữ liệu role
            if(roleRepository.count() == 0){
                for(RoleEnum roleEnum : RoleEnum.values()){
                    Role role = Role.builder()
                            .id(roleEnum)
                            .description(roleEnum.getDescription())
                            .build();
                    roleRepository.save(role);
                }
            }
            
            // Khởi tạo tài khoản admin
            if (accountRepository.findById("admin").isEmpty()){
                Account account = Account.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(RoleEnum.ADMIN)
                        .build();

                accountRepository.save(account);
            }
        };
    }


}
