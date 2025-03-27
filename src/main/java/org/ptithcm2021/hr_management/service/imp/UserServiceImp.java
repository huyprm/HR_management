package org.ptithcm2021.hr_management.service.imp;

import jakarta.mail.MessagingException;
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
import org.ptithcm2021.hr_management.service.MailService;
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
    private final MailService mailService;


    @Override
    public UserResponse createUser(UserRequest userRequest) throws MessagingException {
        User user = userMapper.toUser(userRequest);

        Role role = roleRepository.findById(userRequest.getRoleId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Account account = Account.builder()
                .username(user.getEmail())
                .password(passwordEncoder.encode("123456"))
                .role(role).build();
        User result = userRepository.save(user);

        String message = createSendPWMessage(account.getUsername(), account.getPassword(), result.getFullName());

        mailService.sendMimeEmail(result.getEmail(), message, "Thông Tin Tài Khoản");
        return userMapper.toUserResponse(result);
    }

    @Override
    public UserResponse getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    private String createSendPWMessage(String username, String password, String name){
        return String.format(
                "Xin chào %s,\n\n" +
                        "Tài khoản của bạn đã được tạo thành công.\n" +
                        "Thông tin đăng nhập:\n" +
                        "Tên đăng nhập: %s\n" +
                        "Mật khẩu: %s\n\n" +
                        "Vui lòng đổi mật khẩu ngay sau khi đăng nhập để bảo vệ tài khoản của bạn.\n",
                name, username, password
        );
    }
}
