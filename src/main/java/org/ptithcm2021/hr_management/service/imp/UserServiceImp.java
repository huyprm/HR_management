package org.ptithcm2021.hr_management.service.imp;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

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

        Random random = new Random();
        String password = String.format("%08d", random.nextInt(100_000_000));

        Account account = Account.builder()
                .username(user.getEmail())
                .password(passwordEncoder.encode(password))
                .role(role).build();

        user.setAccount(account);
        User result = userRepository.save(user);

        String message = createSendPWMessage(account.getUsername(), password, result.getFullName());

        mailService.sendMimeEmail(result.getEmail(), message, "Thông Tin Tài Khoản");
        return userMapper.toUserResponse(result);
    }

    @Override
    public UserResponse getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userRequest);

        if(userRequest.getRoleId() != null){
            Role role = roleRepository.findById(userRequest.getRoleId())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

            user.getAccount().setRole(role);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(UserStatusEnum.TERMINATED);
        user.getAccount().setStatus(false);
    }

    @Override
    public UserResponse fetchInfoUser() {
        String securityContext = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(Long.parseLong(securityContext)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Account account =user.getAccount();

        if(account.getPassword().matches(changePasswordRequest.getOldPass())){
            account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPass()));
        }else {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }


    private String createSendPWMessage(String username, String password, String name){
        return String.format(
                "<html>" +
                        "<body>" +
                        "<p>Xin chào <b>%s</b>,</p>" +
                        "<p>Tài khoản của bạn đã được tạo thành công.</p>" +
                        "<p><strong>Thông tin đăng nhập:</strong></p>" +
                        "<ul>" +
                        "<li><b>Tên đăng nhập:</b> %s</li>" +
                        "<li><b>Mật khẩu:</b> %s</li>" +
                        "</ul>" +
                        "<p style='color:red;'><strong>Vui lòng đổi mật khẩu ngay sau khi đăng nhập để bảo vệ tài khoản của bạn.</strong></p>" +
                        "</body>" +
                        "</html>",
                name, username, password
        );
    }
}
