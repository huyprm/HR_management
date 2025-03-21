package org.ptithcm2021.hr_management.service.imp;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.UserMapper;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.Role;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.RoleRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);

        Role role = roleRepository.findById(userRequest.getRoleId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Account account = Account.builder()
                .username(user.getEmail())
                .password(passwordEncoder.encode("123456"))
                .role(role).build();
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
