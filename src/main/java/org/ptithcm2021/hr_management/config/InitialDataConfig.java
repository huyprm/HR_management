package org.ptithcm2021.hr_management.config;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.PositionRepository;
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
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

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

                Role role = roleRepository.findById(RoleEnum.ADMIN)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

                Position position = Position.builder()
                        .id("QTHT")
                        .name("Quản trị hệ thống")
                        .role(role)
                        .build();

                User user = User.builder()
                        .fullName("system")
                        .account(account)
                        .numberCCCD("000000000000")
                        .phoneNumber("0000000000")
                        .email("system@gmail.com")
                        .position(position)
                        .status(UserStatusEnum.ACTIVE)
                        .build();

                positionRepository.save(position);
                userRepository.save(user);
            }
        };
    }


}
