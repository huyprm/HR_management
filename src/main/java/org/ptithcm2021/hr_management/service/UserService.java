package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

public interface UserService {
    @PreAuthorize("hasAuthority('SCOPE_STAFF')")
    UserResponse createUser(UserRequest userRequest) throws MessagingException;

    @PreAuthorize("#id == authentication.name")
    UserResponse getUser(long id);
}
