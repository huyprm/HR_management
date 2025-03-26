package org.ptithcm2021.hr_management.controller;

import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.service.imp.AuthenticationServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

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

    @GetMapping("/forgotPassword")
    public ApiResponse<Void> forgotPassword(@RequestParam String email) throws MessagingException {
        authenticationService.forgotPassword(email);
        return ApiResponse.<Void>builder().message("Successful").build();
    }

    @GetMapping("/verifyOTP")
    public ApiResponse<Boolean> verifyOTP(@RequestParam String email, @RequestParam String otp){
        return ApiResponse.<Boolean>builder().data(authenticationService.verifyOTP(email, otp)).build();
    }

    @PostMapping("/resetPassword")
    public ApiResponse<Account> resetPassword(@RequestParam String newPass, @RequestParam String email){
        return ApiResponse.<Account>builder().data(authenticationService.resetPassword(newPass, email)).build();
    }
}
