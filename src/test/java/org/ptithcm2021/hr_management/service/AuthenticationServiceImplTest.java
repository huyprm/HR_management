package org.ptithcm2021.hr_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.hr_management.dto.request.LoginRequest;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.repository.AccountRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.impl.AuthenticationServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    
    @Mock
    private MailService mailService;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    
    @BeforeEach
    public void setup() {
        // Set private fields using ReflectionTestUtils
        ReflectionTestUtils.setField(authenticationService, "SIGNER_KEY",
                "thisIsASecretSignerKeyThatIsLongEnoughForHS512AlgorithmTesting123456789012345678901234567890");
        ReflectionTestUtils.setField(authenticationService, "expiration", 3600); // 1 hour
    }
    
    @Test
    public void loginSuccess() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        Account account = Account.builder()
                .username("admin")
                .password("encodedPassword")
                .status(true)
                .role(RoleEnum.ADMIN)
                .build();
        
        when(accountRepository.findById("admin")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        
        // Act
        String token = authenticationService.login(loginRequest);
        
        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }
    
    @Test
    public void loginWithNonExistentUsername() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("nonexistent", "password");
        
        when(accountRepository.findById("nonexistent")).thenReturn(Optional.empty());
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.login(loginRequest);
        });
        
        assertEquals(ErrorCode.USERNAME_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    public void loginWithLockedAccount() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("lockedUser", "password");
        Account account = Account.builder()
                .username("lockedUser")
                .password("encodedPassword")
                .status(false) // Locked account
                .role(RoleEnum.USER)
                .build();
        
        when(accountRepository.findById("lockedUser")).thenReturn(Optional.of(account));
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.login(loginRequest);
        });
        
        assertEquals(ErrorCode.ACCOUNT_LOCKED, exception.getErrorCode());
    }
    
    @Test
    public void loginWithWrongPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("admin", "wrongPassword");
        Account account = Account.builder()
                .username("admin")
                .password("encodedPassword")
                .status(true)
                .role(RoleEnum.ADMIN)
                .build();
        
        when(accountRepository.findById("admin")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            authenticationService.login(loginRequest);
        });
        
        assertEquals(ErrorCode.WRONG_PASSWORD, exception.getErrorCode());
    }
    
    // Add more tests for other methods: logout, forgotPassword, verifyOTP, resetPassword, etc.
}