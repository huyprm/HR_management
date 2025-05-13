package org.ptithcm2021.hr_management.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
import org.ptithcm2021.hr_management.dto.request.UserRequest;
import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
import org.ptithcm2021.hr_management.dto.response.UserResponse;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.enums.UserStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.UserMapper;
import org.ptithcm2021.hr_management.model.Account;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.*;
import org.ptithcm2021.hr_management.service.impl.UserServiceImpl;
import org.ptithcm2021.hr_management.util.LeaveApplicationUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InvalidClassException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;
    
    @Mock
    private NotificationRecipientRepository notificationRecipientRepository;

    @Mock
    private WorkLogRepository workLogRepository;

    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @Mock
    private LeaveApplicationUtil leaveApplicationUtil;

    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;

    private User mockUser;
    private UserResponse mockUserResponse;
    
    @BeforeEach
    public void setup() {
        // Common setup for tests
        mockUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .numberCCCD("123456789012")
                .status(UserStatusEnum.ACTIVE)
                .account(Account.builder().username("test_user").build())
                .build();
        
        mockUserResponse = new UserResponse();
        mockUserResponse.setId(1L);
        mockUserResponse.setEmail("test@example.com");
        mockUserResponse.setFullName("Test User");
        mockUserResponse.setNumberCCCD("123456789012");
        mockUserResponse.setStatus(UserStatusEnum.ACTIVE);
        
        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    public void getUserSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);
        
        // Act
        UserResponse result = userService.getUser(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getFullName());
        verify(userRepository, times(1)).findById(1L);
    }
    
    @Test
    public void getUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            userService.getUser(999L);
        });
        
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    public void createUserSuccess() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("new@example.com");
        userRequest.setFullName("New User");
        userRequest.setNumberCCCD("123456789012");

        when(userMapper.toUser(userRequest)).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);
        
        // Act
        UserResponse result = userService.createUser(userRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals(mockUserResponse.getId(), result.getId());
        assertEquals(mockUserResponse.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(mailService, times(1)).sendMimeEmail(any(), any(), any());
    }
    
    @Test
    public void createUserDuplicateEmail() throws MessagingException {
        // Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("existing@example.com");

        when(userRepository.existsUserByEmail(any())).thenReturn(true);
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            userService.createUser(userRequest);
        });

        assertEquals(ErrorCode.EMAIL_EXISTED, exception.getErrorCode());

    }
    
    @Test
    public void updateUserSuccess() {
        // Arrange
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFullName("Updated Name");
        updateRequest.setPhoneNumber("0123456789");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);
        
        // Act
        UserResponse result = userService.updateUser(1L, updateRequest);
        
        // Assert
        assertNotNull(result);
        verify(userMapper, times(1)).updateUser(eq(mockUser), eq(updateRequest));
        verify(userRepository, times(1)).save(mockUser);
    }
    
    @Test
    public void deleteUserSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        
        // Act
        userService.deleteUser(1L);
        
        // Assert
        verify(userRepository, times(1)).save(mockUser);
        assertEquals(UserStatusEnum.TERMINATED, mockUser.getStatus());
    }
    
    @Test
    public void getAllUserByStatusSuccess() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(mockUser);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(userRepository.findAllByStatus(UserStatusEnum.ACTIVE, pageable)).thenReturn(userPage);
        
        // Act
        PagedModel<UserResponse> result = userService.getAllUserByStatus(UserStatusEnum.ACTIVE, pageable);
        
        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findAllByStatus(UserStatusEnum.ACTIVE, pageable);
    }
    
    @Test
    public void changePasswordSuccess() {
        // Arrange
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUserId(1L);
        request.setOldPass("oldPassword");
        request.setNewPass("newPassword");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(eq("oldPassword"), any())).thenReturn(true);
        
        // Act
        userService.changePassword(request);
        
        // Assert
        verify(passwordEncoder, times(1)).encode("newPassword");
        verify(userRepository, times(1)).save(mockUser);
    }
    
    @Test
    public void changePasswordWrongOldPassword() {
        // Arrange
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUserId(1L);
        request.setOldPass("wrongPassword");
        request.setNewPass("newPassword");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(eq("wrongPassword"), any())).thenReturn(false);
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            userService.changePassword(request);
        });
        
        assertEquals(ErrorCode.PASSWORD_NOT_MATCH, exception.getErrorCode());
    }
    
    @Test
    public void fetchInfoUserSuccess() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[] {"AWARD", 0});  // Thêm phần tử cho "AWARD"
        mockResult.add(new Object[] {"DISCIPLINE", 0});  // Thêm phần tử
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1"); // UserId as string
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserResponse(mockUser)).thenReturn(mockUserResponse);
        when(notificationRecipientRepository.findAllByUserId(anyLong(), any())).thenReturn(Page.empty());
        when(leaveBalanceRepository.findByUserIdAndYearAndMonth(anyLong(), anyInt(), anyInt())).thenReturn(Optional.empty());
        when(leaveApplicationUtil.calculateTotalLeveDays(1L, YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth())).thenReturn(0.0);
        when(workLogRepository.countRewardAndDisciplineByUserId(1L)).thenReturn(mockResult);
        // Act
        UserResponse result = userService.fetchInfoUser();
        
        // Assert
        assertNotNull(result);
        assertEquals(mockUserResponse.getId(), result.getId());
        assertEquals(mockUserResponse.getEmail(), result.getEmail());
    }
    
    // Additional tests can be added for other methods:
    // - getWorkLogByUserId
    // - getAllUserByRole
    // - getAllUserByContract
    // - searchUser
}