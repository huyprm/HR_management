package org.ptithcm2021.hr_management.service;

import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.springframework.stereotype.Service;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
}
