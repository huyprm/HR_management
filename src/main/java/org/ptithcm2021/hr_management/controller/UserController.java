package org.ptithcm2021.hr_management.controller;

import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUser(@RequestBody UserRequest request) throws MessagingException {
        return ApiResponse.<UserResponse>builder().data(userService.createUser(request)).build();
    }

    @PostMapping("/update")
    public User updateUser(@RequestBody UserRequest request) {
        return null;
    }


    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable long id){
        return ApiResponse.<UserResponse>builder().data(userService.getUser(id)).build();
    }
}
