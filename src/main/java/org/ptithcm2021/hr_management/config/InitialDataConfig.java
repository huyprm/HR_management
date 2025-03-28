package org.ptithcm2021.hr_management.config;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.Role;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.RoleRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(){
        return args -> {
            if(roleRepository.count() == 0){
                for(RoleEnum roleEnum : RoleEnum.values()){
                    Role role = Role.builder()
                            .id(roleEnum)
                            .description(roleEnum.getDescription())
                            .build();
                    roleRepository.save(role);
                }

            }
            if (accountRepository.findById("admin").isEmpty()){
                Role role = roleRepository.findById(RoleEnum.ADMIN)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

                Account account = Account.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(role)
                        .build();

                accountRepository.save(account);
            }

        };
    }
}
