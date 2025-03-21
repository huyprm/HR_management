package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
private final UserService userService;
    @PostMapping("/create")
    public User createUser(@RequestBody UserRequest request) {
        return  null;
    }

    @PostMapping("/update")
    public User updateUser(@RequestBody UserRequest request) {
        return null;
    }


}
