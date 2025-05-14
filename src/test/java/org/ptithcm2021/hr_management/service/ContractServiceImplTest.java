package org.ptithcm2021.hr_management.service;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.enums.RoleEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.ContractMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.repository.ContractTypeRepository;
import org.ptithcm2021.hr_management.repository.JobGradeRepository;
import org.ptithcm2021.hr_management.repository.PositionRepository;
import org.ptithcm2021.hr_management.service.impl.ContractServiceImpl;
import org.ptithcm2021.hr_management.util.FillDocxWithTagsUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedModel;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;
    
    @Mock
    private ContractMapper contractMapper;
    
    @Mock
    private UserService userService;
    
    @Mock
    private PositionRepository positionRepository;
    
    @Mock
    private ContractTypeRepository contractTypeRepository;
    
    @Mock
    private JobGradeRepository jobGradeRepository;
    
    @Mock
    private FileService fileService;
    
    @InjectMocks
    private ContractServiceImpl contractService;

    private User mockUser;
    private User mockSigner;
    private Position mockPosition;
    private ContractType mockContractType;
    private JobGrade mockJobGrade;
    private Contract mockContract;
    private ContractResponse mockContractResponse;
    private ContractResponse mockContractResponse2;
    private ContractRequest mockContractRequest;
    
    @BeforeEach
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        // Setup test data
        Field field = FillDocxWithTagsUtil.class.getDeclaredField("URL_FORM_FIELDS");
        field.setAccessible(true);
        field.set(null, "https://drive.google.com/uc?id=1kIVhDGE8Gs3pjSfzUZDIYOdZl-kTURwM");

        Position position = Position.builder()
                .id("TP")
                .role(Role.builder().id(RoleEnum.ADMIN).build())
                .build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFullName("John Doe");
        mockUser.setDob(LocalDate.now());


        mockSigner = new User();
        mockSigner.setId(2L);
        mockSigner.setFullName("John Huy");
        mockSigner.setPosition(position);


        mockPosition = Position.builder()
                .id("POS001")
                .name("Software Engineer")
                .build();
                
        mockContractType = ContractType.builder()
                .id("CT001")
                .name("Full-time")
                .build();
        
        mockJobGrade = JobGrade.builder()
                .id("JG001")
                .name("Senior")
                .build();

        mockContractType = ContractType.builder()
                .id("C001")
                .name("Contract one year")
                .build();
                
        mockContract = Contract.builder()
                .id(1)
                .contractType(mockContractType)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusMonths(12))
                .contractStatusEnum(ContractStatusEnum.ACTIVE)
                .basicSalary(10000000)
                .user(mockUser)
                .signer(mockSigner)
                .position(mockPosition)
                .contractType(mockContractType)
                .jobGrade(mockJobGrade)
                .build();

        mockContractResponse2 = new ContractResponse();
        mockContractResponse2.setId(2);
        mockContractResponse2.setContractTypeName(mockContractType.getName());
        mockContractResponse2.setStartDate(LocalDate.now().plusDays(1));
        mockContractResponse2.setEndDate(LocalDate.now().plusMonths(12));
        mockContractResponse2.setContractStatusEnum(ContractStatusEnum.ACTIVE);
        mockContractResponse2.setBasicSalary(10000000);
        mockContractResponse2.setClause("abcdkjlkdfldafjsdkfs");

        mockContractResponse = new ContractResponse();
        mockContractResponse.setId(1);
        mockContractResponse.setContractTypeName(mockContractType.getName());
        mockContractResponse.setStartDate(LocalDate.now().plusDays(1));
        mockContractResponse.setEndDate(LocalDate.now().plusMonths(12));
        mockContractResponse.setContractStatusEnum(ContractStatusEnum.PENDING);
        mockContractResponse.setBasicSalary(10000000);
        mockContractResponse.setClause("abcdkjlkdfldafjsdkfs");
        
        mockContractRequest = new ContractRequest();
        mockContractRequest.setUserId(1L);
        mockContractRequest.setSignerId(2L);
        mockContractRequest.setPositionId("POS001");
        mockContractRequest.setContractTypeId("CT001");
        mockContractRequest.setJobGradeId("JG001");
        mockContractRequest.setStartDate(LocalDate.now().plusDays(1));
        mockContractRequest.setEndDate(LocalDate.now().plusMonths(12));
        mockContractRequest.setBasicSalary(10000000.0);
    }
    
    @Test
    public void createDraftContractSuccess() throws Exception {
        Map<String, String> data = new HashMap<>();

        data.put("location", "TP.HCM");
        data.put("date", "14");
        data.put("month", "5");
        data.put("year", "2025");
        data.put("id", "1");
        data.put("fullNameA", "Nguyễn Văn A");
        data.put("positionA", "Giám đốc");
        data.put("addressA", "123 Đường ABC, TP.HCM");
        data.put("phoneA", "0123456789");
        data.put("fullNameB", "Trần Thị B");
        data.put("dobB", "1995-05-20");
        data.put("nationality", "Việt Nam");
        data.put("addressB", "456 Đường XYZ, TP.HCM");
        data.put("cccd", "123456789012");
        data.put("typeContract", "Hợp đồng lao động chính thức");
        data.put("duration", "12 tháng");
        data.put("startDate", "1");
        data.put("startMonth", "JANUARY"); // Vì bạn dùng .getMonth(), không phải getMonthValue()
        data.put("startYear", "2025");
        data.put("endDate", "31");
        data.put("endMonth", "DECEMBER");
        data.put("endYear", "2025");
        data.put("departmentName", "Phòng Kỹ thuật");
        data.put("positionB", "Kỹ sư phần mềm");
        data.put("salary", "25000000.0"); // VD: basicSalary = 10tr, hệ số 2.5

        // Arrange
        when(userService.getUserToUser(1L)).thenReturn(mockUser);
        when(positionRepository.findById("POS001")).thenReturn(Optional.of(mockPosition));
        when(contractTypeRepository.findById("CT001")).thenReturn(Optional.of(mockContractType));
        when(jobGradeRepository.findById("JG001")).thenReturn(Optional.of(mockJobGrade));
        when(userService.getUserToUser(2L)).thenReturn(mockSigner);
        when(contractRepository.save(any(Contract.class))).thenReturn(mockContract);
        when(contractMapper.toContract(mockContractRequest)).thenReturn(new Contract());
        when(contractMapper.toContractResponse(mockContract)).thenReturn(mockContractResponse);
        doReturn("someValue").when(fileService).uploadFileFromByteArrayOutputStream(any(), eq("HD000"));

        // Act
        ContractResponse result = contractService.createDraftContract(mockContractRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Contract one year", result.getContractTypeName());
        assertEquals(ContractStatusEnum.PENDING, result.getContractStatusEnum());
        verify(contractRepository, times(2)).save(any(Contract.class));
    }
    
    @Test
    public void createDraftContractExistingActiveContract() {
        List<Contract> contracts = new ArrayList<>();
        contracts.add(mockContract);
        // Arrange
        when(contractRepository.findContractByUserId(1L)).thenReturn(contracts);
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            contractService.createDraftContract(mockContractRequest);
        });
        
        assertEquals(ErrorCode.CONTRACT_OVERLAP, exception.getErrorCode());
    }
    
    @Test
    public void getContractSuccess() {
        // Arrange
        when(contractRepository.findById(1)).thenReturn(Optional.of(mockContract));
        when(contractMapper.toContractResponse(mockContract)).thenReturn(mockContractResponse);
        
        // Act
        ContractResponse result = contractService.getContract(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Contract one year", result.getContractTypeName());
    }
    
    @Test
    public void getContractNotFound() {
        // Arrange
        when(contractRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            contractService.getContract(999);
        });
        
        assertEquals(ErrorCode.CONTRACT_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    public void getAllContractSuccess() {
        // Arrange
        List<Contract> contracts = new ArrayList<>();
        contracts.add(mockContract);
        Page<Contract> contractPage = new PageImpl<>(contracts);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(contractRepository.findAllContractByContractStatusEnum(eq(ContractStatusEnum.ACTIVE), eq(pageable))).thenReturn(contractPage);
        //when(pagedResourcesAssembler.toModel(eq(contractPage), any())).thenReturn(PagedModel.empty());
        
        // Act
        PagedModel<ContractResponse> result = contractService.getAllContract(ContractStatusEnum.ACTIVE, pageable);
        
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllContractByContractStatusEnum(ContractStatusEnum.ACTIVE, pageable);
    }
    
    @Test
    public void getContractIsActiveByUserSuccess() {
        // Arrange
        when(contractRepository.findContractByUserIdAndContractStatusEnum(1L, ContractStatusEnum.ACTIVE)).thenReturn(Optional.of(mockContract));
        when(contractMapper.toContractResponse(mockContract)).thenReturn(mockContractResponse2);
        
        // Act
        ContractResponse result = contractService.getContractIsActiveByUser(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getId());
        assertEquals(ContractStatusEnum.ACTIVE, result.getContractStatusEnum());
    }
    
    @Test
    public void getContractIsActiveByUserNotFound() {
        // Arrange
        when(contractRepository.findContractByUserIdAndContractStatusEnum(1L, ContractStatusEnum.ACTIVE)).thenReturn(Optional.empty());
        
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> {
            contractService.getContractIsActiveByUser(1L);
        });
        
        assertEquals(ErrorCode.CONTRACT_NOT_FOUND, exception.getErrorCode());
    }
    
    @Test
    public void getContractsByUserIdAndStatusNotActive() {
        // Arrange
        List<Contract> contracts = new ArrayList<>();
        contracts.add(mockContract);
        
        when(contractRepository.findAllContractByUserIdIsNotActive(1L)).thenReturn(contracts);
        when(contractMapper.toContractResponse(mockContract)).thenReturn(mockContractResponse);
        
        // Act
        List<ContractResponse> results = contractService.getContractsByUserIdAndStatusNotActive(1L);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getId());
    }
    
    // Additional tests can be added for:
    // - signContract
    // - extendContract
    // - updateContract
    // - deleteContract
    // - updateContractWithPromotion
}