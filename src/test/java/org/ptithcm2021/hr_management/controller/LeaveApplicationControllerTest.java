//package org.ptithcm2021.hr_management.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.ptithcm2021.hr_management.dto.request.LeaveApplicationRequest;
//import org.ptithcm2021.hr_management.dto.response.LeaveApplicationResponse;
//import org.ptithcm2021.hr_management.dto.response.UserSummaryResponse;
//import org.ptithcm2021.hr_management.enums.FormStatusEnum;
//import org.ptithcm2021.hr_management.service.LeaveApplicationService;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//public class LeaveApplicationControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private LeaveApplicationService leaveApplicationService;
//
//    @Mock
//    private SecurityContext securityContext;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private LeaveApplicationController leaveApplicationController;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(leaveApplicationController).build();
//        objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule()); // Support for Java 8 date/time types
//
//        // Mock SecurityContextHolder
//        SecurityContextHolder.setContext(securityContext);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("1"); // User ID
//    }
//
//    @Test
//    public void createApplicationSuccess() throws Exception {
//        // Arrange
//        LeaveApplicationRequest request = new LeaveApplicationRequest();
//        request.setUserId(1L);
//        request.setLeaveTypeId(1);
//        request.setStartDate(LocalDate.now().plusDays(1));
//        request.setEndDate(LocalDate.now().plusDays(3));
//        request.setReason("Vacation");
//
//        LeaveApplicationResponse response = new LeaveApplicationResponse();
//        response.setId(1L);
//        response.setStartDate(LocalDate.now().plusDays(1));
//        response.setEndDate(LocalDate.now().plusDays(3));
//        response.setReason("Vacation");
//        response.setFormStatusEnum(FormStatusEnum.PENDING);
//        response.setLeaveTypeName("Annual Leave");
//
//        UserSummaryResponse userSummary = new UserSummaryResponse();
//        userSummary.setId(1L);
//        userSummary.setFullName("Test User");
//        response.setUser(userSummary);
//
//        when(leaveApplicationService.createApplication(any(LeaveApplicationRequest.class))).thenReturn(response);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/leave-applications/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.reason", is("Vacation")))
//                .andExpect(jsonPath("$.data.formStatusEnum", is("PENDING")))
//                .andExpect(jsonPath("$.data.leaveTypeName", is("Annual Leave")))
//                .andExpect(jsonPath("$.data.user.id", is(1)))
//                .andExpect(jsonPath("$.data.user.fullName", is("Test User")));
//
//        verify(leaveApplicationService, times(1)).createApplication(any(LeaveApplicationRequest.class));
//    }
//
//    @Test
//    public void confirmApplicationSuccess() throws Exception {
//        // Arrange
//        LeaveApplicationResponse response = new LeaveApplicationResponse();
//        response.setId(1L);
//        response.setFormStatusEnum(FormStatusEnum.APPROVED);
//
//        UserSummaryResponse userSummary = new UserSummaryResponse();
//        userSummary.setId(1L);
//        userSummary.setFullName("Test User");
//        response.setUser(userSummary);
//
//        UserSummaryResponse signerSummary = new UserSummaryResponse();
//        signerSummary.setId(2L);
//        signerSummary.setFullName("Manager User");
//        response.setSigner(signerSummary);
//
//        when(leaveApplicationService.confirmApplication(eq(FormStatusEnum.APPROVED), eq(1L))).thenReturn(response);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/leave-applications/confirm/1")
//                .param("formStatusEnum", "APPROVED"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.formStatusEnum", is("APPROVED")))
//                .andExpect(jsonPath("$.data.user.id", is(1)))
//                .andExpect(jsonPath("$.data.signer.id", is(2)));
//
//        verify(leaveApplicationService, times(1)).confirmApplication(FormStatusEnum.APPROVED, 1L);
//    }
//
//    @Test
//    public void getApplicationIsPendingSuccess() throws Exception {
//        // Arrange
//        List<LeaveApplicationResponse> applications = new ArrayList<>();
//        LeaveApplicationResponse application = new LeaveApplicationResponse();
//        application.setId(1L);
//        application.setFormStatusEnum(FormStatusEnum.PENDING);
//        applications.add(application);
//
//        when(leaveApplicationService.getApplicationIsPending(FormStatusEnum.PENDING)).thenReturn(applications);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/leave-applications")
//                .param("formStatusEnum", "PENDING"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data", hasSize(1)))
//                .andExpect(jsonPath("$.data[0].id", is(1)))
//                .andExpect(jsonPath("$.data[0].formStatusEnum", is("PENDING")));
//
//        verify(leaveApplicationService, times(1)).getApplicationIsPending(FormStatusEnum.PENDING);
//    }
//
//    @Test
//    public void getApplicationSuccess() throws Exception {
//        // Arrange
//        LeaveApplicationResponse response = new LeaveApplicationResponse();
//        response.setId(1L);
//        response.setFormStatusEnum(FormStatusEnum.APPROVED);
//
//        when(leaveApplicationService.getApplication(1L)).thenReturn(response);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/leave-applications/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.formStatusEnum", is("APPROVED")));
//
//        verify(leaveApplicationService, times(1)).getApplication(1L);
//    }
//
//    @Test
//    public void getApplicationByUserSuccess() throws Exception {
//        // Arrange
//        List<LeaveApplicationResponse> applications = new ArrayList<>();
//        LeaveApplicationResponse application1 = new LeaveApplicationResponse();
//        application1.setId(1L);
//        application1.setFormStatusEnum(FormStatusEnum.APPROVED);
//
//        LeaveApplicationResponse application2 = new LeaveApplicationResponse();
//        application2.setId(2L);
//        application2.setFormStatusEnum(FormStatusEnum.PENDING);
//
//        applications.add(application1);
//        applications.add(application2);
//
//        when(leaveApplicationService.getApplicationByUserId(1L)).thenReturn(applications);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/leave-applications/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data", hasSize(2)))
//                .andExpect(jsonPath("$.data[0].id", is(1)))
//                .andExpect(jsonPath("$.data[0].formStatusEnum", is("APPROVED")))
//                .andExpect(jsonPath("$.data[1].id", is(2)))
//                .andExpect(jsonPath("$.data[1].formStatusEnum", is("PENDING")));
//
//        verify(leaveApplicationService, times(1)).getApplicationByUserId(1L);
//    }
//}
