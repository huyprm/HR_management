package org.ptithcm2021.hr_management.controller;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {

        return ApiResponse.<String>builder()
                .data(authenticationService.login(request)).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody String token) throws ParseException {
        authenticationService.logout(token);
        return ApiResponse.<Void>builder().build();
    }


}
