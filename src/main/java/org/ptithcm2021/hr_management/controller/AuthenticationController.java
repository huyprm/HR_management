package org.ptithcm2021.hr_management.controller;

import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.ptithcm2021.hr_management.service.AuthenticationService;
import org.ptithcm2021.hr_management.service.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest request){
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
    public ApiResponse<String> resetPassword(@RequestParam String newPass, @RequestParam String email){
        return ApiResponse.<String>builder().data(authenticationService.resetPassword(newPass, email)).build();
    }

    @GetMapping("/role")
    public ApiResponse<String> getRole(@RequestParam String token) throws ParseException, JOSEException {
        return ApiResponse.<String>builder().data(authenticationService.getRoleByToken(token)).build();
    }
}
