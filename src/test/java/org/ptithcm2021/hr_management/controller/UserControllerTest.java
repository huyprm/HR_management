//package org.ptithcm2021.hr_management.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.ptithcm2021.hr_management.dto.request.ChangePasswordRequest;
//import org.ptithcm2021.hr_management.dto.request.UserRequest;
//import org.ptithcm2021.hr_management.dto.request.UserUpdateRequest;
//import org.ptithcm2021.hr_management.dto.response.ApiResponse;
//import org.ptithcm2021.hr_management.dto.response.ContractResponse;
//import org.ptithcm2021.hr_management.dto.response.UserResponse;
//import org.ptithcm2021.hr_management.dto.response.WorkLogResponse;
//import org.ptithcm2021.hr_management.enums.RoleEnum;
//import org.ptithcm2021.hr_management.enums.UserStatusEnum;
//import org.ptithcm2021.hr_management.service.UserService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PagedModel;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void createUserSuccess() throws Exception {
//        // Arrange
//        UserRequest userRequest = new UserRequest();
//        userRequest.setEmail("test@example.com");
//        userRequest.setFullName("Test User");
//        userRequest.setNumberCCCD("123456789012");
//
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(1L);
//        userResponse.setEmail("test@example.com");
//        userResponse.setFullName("Test User");
//        userResponse.setNumberCCCD("123456789012");
//
//        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/users/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.email", is("test@example.com")))
//                .andExpect(jsonPath("$.data.fullName", is("Test User")));
//
//        verify(userService, times(1)).createUser(any(UserRequest.class));
//    }
//
//    @Test
//    public void getUserSuccess() throws Exception {
//        // Arrange
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(1L);
//        userResponse.setEmail("test@example.com");
//        userResponse.setFullName("Test User");
//        userResponse.setStatus(UserStatusEnum.ACTIVE);
//
//        when(userService.getUser(1L)).thenReturn(userResponse);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.email", is("test@example.com")))
//                .andExpect(jsonPath("$.data.status", is("ACTIVE")));
//
//        verify(userService, times(1)).getUser(1L);
//    }
//
//    @Test
//    public void updateUserSuccess() throws Exception {
//        // Arrange
//        UserUpdateRequest updateRequest = new UserUpdateRequest();
//        updateRequest.setFullName("Updated Name");
//        updateRequest.setPhoneNumber("0123456789");
//
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(1L);
//        userResponse.setFullName("Updated Name");
//        userResponse.setPhoneNumber("0123456789");
//
//        when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(userResponse);
//
//        // Act & Assert
//        mockMvc.perform(patch("/api/users/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.fullName", is("Updated Name")))
//                .andExpect(jsonPath("$.data.phoneNumber", is("0123456789")));
//
//        verify(userService, times(1)).updateUser(eq(1L), any(UserUpdateRequest.class));
//    }
//
//    @Test
//    public void deleteUserSuccess() throws Exception {
//        // Arrange
//        doNothing().when(userService).deleteUser(1L);
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/users/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", is("User has been deleted")));
//
//        verify(userService, times(1)).deleteUser(1L);
//    }
//
//    @Test
//    public void fetchInfoUserSuccess() throws Exception {
//        // Arrange
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(1L);
//        userResponse.setEmail("test@example.com");
//        userResponse.setFullName("Test User");
//
//        when(userService.fetchInfoUser()).thenReturn(userResponse);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/users/info"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.id", is(1)))
//                .andExpect(jsonPath("$.data.email", is("test@example.com")));
//
//        verify(userService, times(1)).fetchInfoUser();
//    }
//
//    @Test
//    public void changePasswordSuccess() throws Exception {
//        // Arrange
//        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
//        changePasswordRequest.setUserId(1L);
//        changePasswordRequest.setOldPass("oldPassword");
//        changePasswordRequest.setNewPass("newPassword");
//
//        doNothing().when(userService).changePassword(any(ChangePasswordRequest.class));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/users/change-pass")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(changePasswordRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", is("Change successful")));
//
//        verify(userService, times(1)).changePassword(any(ChangePasswordRequest.class));
//    }
//
//    @Test
//    public void getWorkLogByUserIdSuccess() throws Exception {
//        // Arrange
//        ContractResponse contractResponse = new ContractResponse();
//        List<WorkLogResponse> workLogs = new ArrayList<>();
//        WorkLogResponse workLog = new WorkLogResponse();
//        workLog.setContract(contractResponse);
//        workLogs.add(workLog);
//
//        when(userService.getWorkLogByUserId(1L)).thenReturn(workLogs);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/users/work-log")
//                .param("userId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data", hasSize(1)))
//                .andExpect(jsonPath("$.data[0].id", is(1)))
//                .andExpect(jsonPath("$.data[0].description", is("Work log entry")));
//
//        verify(userService, times(1)).getWorkLogByUserId(1L);
//    }
//
//    @Test
//    public void getAllUserByRoleSuccess() throws Exception {
//        // Arrange
//        PagedModel<UserResponse> pagedModel = new PagedModel<>(Page.empty());
//
//        when(userService.getAllUserByRole(eq(RoleEnum.STAFF), any(Pageable.class))).thenReturn(pagedModel);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/users/role/STAFF")
//                .param("page", "0")
//                .param("size", "10"))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).getAllUserByRole(eq(RoleEnum.STAFF), any(Pageable.class));
//    }
//
//    // Additional tests can be added for:
//    // - searchUser
//    // - getAllUserByContract
//    // - getAllUserByStatus with invalid validation scenarios
//}