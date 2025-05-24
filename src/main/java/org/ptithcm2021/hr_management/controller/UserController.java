package org.ptithcm2021.hr_management.controller;

import com.cloudinary.Api;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.*;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.projection.UserSummary;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.hibernate.validator.internal.util.ReflectionHelper.typeOf;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) throws MessagingException {
        return ApiResponse.<UserResponse>builder().data(userService.createUser(request)).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable long id){
        return ApiResponse.<UserResponse>builder().data(userService.getUser(id)).build();
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable long id, @RequestBody UserUpdateRequest userRequest){
        if(userRequest.getNumberCCCD() != null && !userRequest.getNumberCCCD().matches("\\d{12}")){
            throw new IllegalArgumentException("CCCD must be 12 characters");
        }

        if(userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().matches("0\\d{9}")){
            throw new IllegalArgumentException("Phone number must be 10 characters");
        }

        return ApiResponse.<UserResponse>builder().data(userService.updateUser(id, userRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ApiResponse.<Void>builder().message("User has been deleted").build();
    }

    @GetMapping("/info")
    public ApiResponse<UserResponse> fetchInfoUser(){
        return ApiResponse.<UserResponse>builder().data(userService.fetchInfoUser()).build();
    }

    @PostMapping("/change-pass")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
        return ApiResponse.<Void>builder().message("Change successful").build();
    }

    @GetMapping()
    public ApiResponse<PagedModel<UserResponse>> getAllUser(@RequestParam(required = false)UserStatusEnum status,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return ApiResponse.<PagedModel<UserResponse>>builder().data(userService.getAllUserByStatus(status, pageable)).build();
    }

    @GetMapping("/work-log")
    public ApiResponse<List<WorkLogResponse>> getWorkLogByUserId(@RequestParam long userId){
        return ApiResponse.<List<WorkLogResponse>>builder().data(userService.getWorkLogByUserId(userId)).build();
    }

    @GetMapping("/role/{roleName}")
    public ApiResponse<PagedModel<UserResponse>> getAllUserByRole(@PathVariable RoleEnum roleName,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return ApiResponse.<PagedModel<UserResponse>>builder().data(userService.getAllUserByRole(roleName, pageable)).build();
    }

    @GetMapping("/search")
    public ApiResponse<List<UserSummary>> searchUser(@RequestParam String keyword){
        return ApiResponse.<List<UserSummary>>builder().data(userService.searchUser(keyword)).build();
    }

    @GetMapping("/contract")
    public ApiResponse<PagedModel<UserResponse>> getAllUserByContract(@RequestParam(required = false) ContractStatusEnum contractStatusEnum,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return ApiResponse.<PagedModel<UserResponse>>builder().data(userService.getAllUserByContract(contractStatusEnum, pageable)).build();
    }


    @PutMapping("/save-device/{userId}")
    public ApiResponse<String> saveDeviceToken(@RequestParam String deviceToken,
                                               @PathVariable long userId){
        return ApiResponse.<String>builder().data(userService.saveDeviceToken(userId,deviceToken)).build();
    }

    @GetMapping("department/{id}")
    public ApiResponse<PagedModel<UserResponse>> getAllDepartments(@PathVariable String id,
                                                                   @RequestParam(required = false) UserStatusEnum status,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size){
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        return ApiResponse.<PagedModel<UserResponse>>builder().data(userService.getAllUserByDepartment(id, status, pageable)).build();
    }

    @PutMapping("/remove-device/{id}")
    public ApiResponse<String> removeDeviceToken(@PathVariable long id){
        userService.removeDeviceToken(id);

        return ApiResponse.<String>builder().message("Deleted device token successful").build();
    }
}
