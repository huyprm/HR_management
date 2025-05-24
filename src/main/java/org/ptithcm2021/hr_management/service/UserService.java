package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.dto.response.WorkLogResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.projection.UserSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {
    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse createUser(UserRequest userRequest) throws MessagingException;

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN', 'SCOPE_MANAGER')")
    UserResponse getUser(long id);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    UserResponse updateUser(long id, UserUpdateRequest userRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
    void deleteUser(long id);

    UserResponse fetchInfoUser();

    @PreAuthorize("T(String).valueOf(#changePasswordRequest.userId) == authentication.name")
    void changePassword(ChangePasswordRequest changePasswordRequest);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN', 'SCOPE_MANAGER')")
    PagedModel<UserResponse> getAllUserByStatus(UserStatusEnum status, Pageable pageable);

    User getUserToUser(long id);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    List<WorkLogResponse> getWorkLogByUserId(long userId);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN', 'SCOPE_MANAGER')")
    PagedModel<UserResponse> getAllUserByRole(RoleEnum roleName, Pageable pageable);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN', 'SCOPE_MANAGER')")
    PagedModel<UserResponse> getAllUserByContract(ContractStatusEnum contractStatusEnum, Pageable pageable);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN', 'SCOPE_MANAGER')")
    List<UserSummary> searchUser(String keyword);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    String saveDeviceToken(long userId, String deviceToken);

    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN', 'SCOPE_MANAGER')")
    PagedModel<UserResponse> getAllUserByDepartment(String departmentId, UserStatusEnum status, Pageable pageable);

    @PreAuthorize("T(String).valueOf(#userId) == authentication.name")
    void removeDeviceToken(long userId);
}
