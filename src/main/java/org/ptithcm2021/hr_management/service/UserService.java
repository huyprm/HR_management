package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;
import org.aspectj.weaver.patterns.IToken;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.dto.response.WorkLogResponse;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse createUser(UserRequest userRequest) throws MessagingException;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse getUser(long id);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse updateUser(long id, UserUpdateRequest userRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteUser(long id);

    UserResponse fetchInfoUser();

    @PreAuthorize("T(String).valueOf(#changePasswordRequest.userId) == authentication.name")
    void changePassword(ChangePasswordRequest changePasswordRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    List<UserResponse> getAllUserByStatus(UserStatusEnum status);

    User getUserToUser(long id);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<WorkLogResponse> getWorkLogByUserId(long userId);
}
