package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;
import org.aspectj.weaver.patterns.IToken;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

public interface UserService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse createUser(UserRequest userRequest) throws MessagingException;

    @PreAuthorize("#id == authentication.name")
    UserResponse getUser(long id);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse updateUser(long id, UserRequest userRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteUser(long id);

    UserResponse fetchInfoUser();
}
