package org.ptithcm2021.hr_management.service;

import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.model.Account;

import java.text.ParseException;

public interface AuthenticationService {
    String generateToken(Account account);
    boolean verifyToken(String token) throws ParseException, JOSEException;
    String login(LoginRequest loginRequest);
    void logout(String token) throws ParseException;
    void forgotPassword(String email) throws MessagingException;
    boolean verifyOTP(String email, String otp);
    Account resetPassword(String newPass, String email);
}
