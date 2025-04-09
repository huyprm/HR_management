package org.ptithcm2021.hr_management.controller;

import com.cloudinary.Api;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.service.UserService;
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
    public ApiResponse<UserResponse> updateUser(@PathVariable long id, @RequestBody @Valid UserRequest userRequest){
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

    @GetMapping("")
    public ApiResponse<List<UserResponse>> getAllUser(){
        return ApiResponse.<List<UserResponse>>builder().data(userService.getAllUser()).build();
    }

    @GetMapping("/1")
    public ApiResponse<List<User>> getAllUser1(){
        return ApiResponse.<List<User>>builder().data(userService.getAllUser1()).build();
    }
}
