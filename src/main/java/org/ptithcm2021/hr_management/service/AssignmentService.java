//package org.ptithcm2021.hr_management.service;
//
//
//import org.ptithcm2021.hr_management.dto.request.AssignmentRequest;
//import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
//import org.springframework.security.access.prepost.PreAuthorize;
//
//import java.util.List;
//
//public interface AssignmentService {
//    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
//    AssignmentResponse createAssignment(AssignmentRequest assignmentRequest);
//
//    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
//    AssignmentResponse updateAssignment(AssignmentRequest assignmentRequest, String rewardDecisionId, long userId);
//
//    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or #userId == authentication.name")
//    AssignmentResponse getAssignment(String rewardId, long userId);
//
//    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN') or #userId == authentication.name")
//    List<AssignmentResponse> getAllAssignmentByUser (long userId);
//
//    @PreAuthorize("hasAnyAuthority('SCOPE_STAFF', 'SCOPE_ADMIN')")
//    void deleteAssignment(String rewardId, long userId);
//}
