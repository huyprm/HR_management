package org.ptithcm2021.hr_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ptithcm2021.hr_management.dto.request.ContractRequest;
import org.ptithcm2021.hr_management.dto.response.ContractResponse;
import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
import org.ptithcm2021.hr_management.exception.AppException;
import org.ptithcm2021.hr_management.exception.ErrorCode;
import org.ptithcm2021.hr_management.mapper.ContractMapper;
import org.ptithcm2021.hr_management.model.*;
import org.ptithcm2021.hr_management.repository.ContractRepository;
import org.ptithcm2021.hr_management.repository.ContractTypeRepository;
import org.ptithcm2021.hr_management.repository.JobGradeRepository;
import org.ptithcm2021.hr_management.repository.PositionRepository;
import org.ptithcm2021.hr_management.service.impl.ContractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
    @Mock
    private PagedResourcesAssembler<Contract> pagedResourcesAssembler;
    
    @InjectMocks
    private ContractServiceImpl contractService;

    private User mockUser;
    private Position mockPosition;
    private ContractType mockContractType;
    private JobGrade mockJobGrade;
    private Contract mockContract;
    private ContractResponse mockContractResponse;
    private ContractRequest mockContractRequest;
    
    @BeforeEach
    public void setup() {
        // Setup test data
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFullName("John Doe");
                
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
                .position(mockPosition)
                .contractType(mockContractType)
                .jobGrade(mockJobGrade)
                .build();
                
        mockContractResponse = new ContractResponse();
        mockContractResponse.setId(1);
        mockContractResponse.setContractTypeName(mockContractType.getName());
        mockContractResponse.setStartDate(LocalDate.now().plusDays(1));
        mockContractResponse.setEndDate(LocalDate.now().plusMonths(12));
        mockContractResponse.setContractStatusEnum(ContractStatusEnum.ACTIVE);
        mockContractResponse.setBasicSalary(10000000);
        mockContractResponse.setClause("abcdkjlkdfldafjsdkfs");
        
        mockContractRequest = new ContractRequest();
        mockContractRequest.setUserId(1L);
        mockContractRequest.setPositionId("POS001");
        mockContractRequest.setContractTypeId("CT001");
        mockContractRequest.setJobGradeId("JG001");
        mockContractRequest.setStartDate(LocalDate.now().plusDays(1));
        mockContractRequest.setEndDate(LocalDate.now().plusMonths(12));
        mockContractRequest.setBasicSalary(10000000.0);
    }
    
    @Test
    public void createDraftContractSuccess() throws Exception {
        // Arrange
        when(userService.getUserToUser(1L)).thenReturn(mockUser);
        when(positionRepository.findById("POS001")).thenReturn(Optional.of(mockPosition));
        when(contractTypeRepository.findById("CT001")).thenReturn(Optional.of(mockContractType));
        when(jobGradeRepository.findById("JG001")).thenReturn(Optional.of(mockJobGrade));
        //when(contractRepository.existsByUserIdAndStatus(1L, ContractStatusEnum.ACTIVE)).thenReturn(false);
        //when(contractRepository.countByContractCode(anyString())).thenReturn(0L);
        when(contractRepository.save(any(Contract.class))).thenReturn(mockContract);
        when(contractMapper.toContractResponse(mockContract)).thenReturn(mockContractResponse);
        
        // Act
        ContractResponse result = contractService.createDraftContract(mockContractRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("C001", result.getContractTypeName());
        assertEquals(ContractStatusEnum.PENDING, result.getContractStatusEnum());
        verify(contractRepository, times(1)).save(any(Contract.class));
    }
    
    @Test
    public void createDraftContractExistingActiveContract() {
        // Arrange
        when(userService.getUserToUser(1L)).thenReturn(mockUser);
        when(positionRepository.findById("POS001")).thenReturn(Optional.of(mockPosition));
        when(contractTypeRepository.findById("CT001")).thenReturn(Optional.of(mockContractType));
        when(jobGradeRepository.findById("JG001")).thenReturn(Optional.of(mockJobGrade));
        when(contractRepository.findContractByUserIdAndContractStatusEnum(1L, ContractStatusEnum.ACTIVE)).thenReturn(Optional.ofNullable(mockContract));
        
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
        assertEquals("C001", result.getContractTypeName());
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
        when(contractMapper.toContractResponse(mockContract)).thenReturn(mockContractResponse);
        
        // Act
        ContractResponse result = contractService.getContractIsActiveByUser(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
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