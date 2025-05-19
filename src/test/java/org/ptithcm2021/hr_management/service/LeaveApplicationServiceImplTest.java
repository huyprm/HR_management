package org.ptithcm2021.hr_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.LeaveApplicationMapper;
import org.ptithcm2021.hr_management.model.LeaveApplication;
import org.ptithcm2021.hr_management.model.LeaveType;
import org.ptithcm2021.hr_management.model.User;
import org.ptithcm2021.hr_management.repository.LeaveApplicationRepository;
import org.ptithcm2021.hr_management.repository.LeaveBalanceRepository;
import org.ptithcm2021.hr_management.repository.LeaveTypeRepository;
import org.ptithcm2021.hr_management.repository.UserRepository;
import org.ptithcm2021.hr_management.service.impl.LeaveApplicationServiceImpl;
import org.ptithcm2021.hr_management.util.LeaveApplicationUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveApplicationServiceImplTest {

    @Mock
    private LeaveApplicationMapper leaveApplicationMapper;
    
    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserService userService;
    
    @Mock
    private LeaveBalanceService leaveBalanceService;
    
    @Mock
    private LeaveTypeRepository leaveTypeRepository;
    
    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;
    
    @Mock
    private LeaveApplicationUtil leaveApplicationUtil;
    
    @Mock
    private SecurityContext securityContext;
    
    @Mock
    private Authentication authentication;
    
    @InjectMocks
    private LeaveApplicationServiceImpl leaveApplicationService;
    
    private User mockUser;
    private User mockManager;
    private LeaveType mockLeaveType;
    private LeaveApplication mockApplication;
    private LeaveApplicationResponse mockResponse;
    private LeaveApplicationRequest mockRequest;
    
    @BeforeEach
    public void setup() {
        // Setup test data
        mockUser = new User();
        mockManager = new User();


        mockUser.setId(1L);
        mockUser.setFullName("Employee");

        mockManager.setId(2L);
        mockManager.setFullName("Manager");
                
        mockLeaveType = LeaveType.builder()
                .id(1)
                .name("Annual Leave")
                .build();
                
        mockApplication = LeaveApplication.builder()
                .id(1L)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(3))
                .reason("Vacation")
                .formStatusEnum(FormStatusEnum.PENDING)
                .user(mockUser)
                .leaveType(mockLeaveType)
                .build();
                
        mockResponse = new LeaveApplicationResponse();
        mockResponse.setId(1L);
        mockResponse.setStartDate(LocalDate.now().plusDays(1));
        mockResponse.setEndDate(LocalDate.now().plusDays(3));
        mockResponse.setReason("Vacation");
        mockResponse.setFormStatusEnum(FormStatusEnum.PENDING);
        
        mockRequest = new LeaveApplicationRequest();
        mockRequest.setUserId(1L);
        mockRequest.setLeaveTypeId(1);
        mockRequest.setStartDate(LocalDate.now().plusDays(1));
        mockRequest.setEndDate(LocalDate.now().plusDays(3));
        mockRequest.setReason("Vacation");
        
        // Mock SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
    }

    //@Test
//    public void createApplicationSuccess() {
//        // Arrange
//        when(leaveApplicationMapper.toLeaveApplication(mockRequest)).thenReturn(mockApplication);
//        when(userService.getUserToUser(1L)).thenReturn(mockUser);
//        when(leaveTypeRepository.findById(1)).thenReturn(Optional.of(mockLeaveType));
//        when(leaveApplicationRepository.save(any(LeaveApplication.class))).thenReturn(mockApplication);
//        when(leaveApplicationMapper.toLeaveTypeApplicationResponse(mockApplication)).thenReturn(mockResponse);
//
//        // Act
//        LeaveApplicationResponse result = leaveApplicationService.createApplication(mockRequest);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("Vacation", result.getReason());
//        assertEquals(FormStatusEnum.PENDING, result.getFormStatusEnum());
//        verify(leaveApplicationRepository, times(1)).save(mockApplication);
//    }
    
//    @Test
//    public void confirmApplicationSuccess() {
//        // Arrange
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("2"); // Manager's ID
//        when(userRepository.findById(2L)).thenReturn(Optional.of(mockManager));
//        when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication));
//        when(leaveApplicationRepository.save(any(LeaveApplication.class))).thenReturn(mockApplication);
//        when(leaveApplicationMapper.toLeaveTypeApplicationResponse(mockApplication)).thenReturn(mockResponse);
//
//        // Act
//        LeaveApplicationResponse result = leaveApplicationService.confirmApplication(FormStatusEnum.APPROVED, 1L);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(FormStatusEnum.APPROVED, mockApplication.getFormStatusEnum());
//        assertEquals(mockManager, mockApplication.getSigner());
//        verify(leaveApplicationRepository, times(1)).save(mockApplication);
//    }
    
    @Test
    public void confirmApplicationSignerIsUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("1"); // Same as applicant ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication));
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            leaveApplicationService.confirmApplication(FormStatusEnum.APPROVED, 1L);
        });
        
        assertEquals(ErrorCode.SIGNER_IS_USER, exception.getErrorCode());
    }
    
//    @Test
//    public void confirmApplicationNotPending() {
//        // Arrange
//        mockApplication.setFormStatusEnum(FormStatusEnum.APPROVED); // Already approved
//
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("2"); // Manager's ID
//        when(userRepository.findById(2L)).thenReturn(Optional.of(mockManager));
//        when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication));
//
//        // Act & Assert
//        AppException exception = assertThrows(AppException.class, () -> {
//            leaveApplicationService.confirmApplication(FormStatusEnum.APPROVED, 1L);
//        });
//
//        assertEquals(ErrorCode.FORM_STATUS_INVALID, exception.getErrorCode());
//    }
    
    @Test
    public void getApplicationSuccess() {
        // Arrange
        when(leaveApplicationRepository.findById(1L)).thenReturn(Optional.of(mockApplication));
        when(leaveApplicationMapper.toLeaveTypeApplicationResponse(any(LeaveApplication.class))).thenReturn(mockResponse);
        
        // Act
        LeaveApplicationResponse result = leaveApplicationService.getApplication(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
    
    @Test
    public void getApplicationNotFound() {
        // Arrange
        when(leaveApplicationRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            leaveApplicationService.getApplication(999L);
        });
        
        assertEquals(ErrorCode.LEAVE_APPLICATION_NOT_FOUND, exception.getErrorCode());
    }
    
//    @Test
//    public void getApplicationIsPending() {
//        // Arrange
//        List<LeaveApplication> applications = new ArrayList<>();
//        applications.add(mockApplication);
//
//        when(leaveApplicationRepository.getAllByFormStatusEnum(FormStatusEnum.PENDING)).thenReturn(applications);
//        when(leaveApplicationMapper.toLeaveTypeApplicationResponse(mockApplication)).thenReturn(mockResponse);
//
//        // Act
//        List<LeaveApplicationResponse> results = leaveApplicationService.getApplicationIsPending(FormStatusEnum.PENDING);
//
//        // Assert
//        assertNotNull(results);
//        assertEquals(1, results.size());
//        assertEquals(1L, results.get(0).getId());
//    }
    
    @Test
    public void getApplicationByUserIdSuccess() {
        // Arrange
        List<LeaveApplication> applications = new ArrayList<>();
        applications.add(mockApplication);
        
        when(leaveApplicationRepository.findAllByUserId(1L)).thenReturn(applications);
        when(leaveApplicationMapper.toLeaveTypeApplicationResponse(mockApplication)).thenReturn(mockResponse);
        
        // Act
        List<LeaveApplicationResponse> results = leaveApplicationService.getApplicationByUserId(1L);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }
    
    @Test
    public void getTotalLeaveDaysByUserId() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);
        double expectedDays = 5.0;
        
        when(leaveApplicationUtil.calculateTotalLeveDays(1L, startDate, endDate)).thenReturn(expectedDays);
        
        // Act
        double result = leaveApplicationService.getTotalLeaveDaysByUserId(1L, startDate, endDate);
        
        // Assert
        assertEquals(expectedDays, result);
    }
}