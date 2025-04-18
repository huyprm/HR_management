//package org.ptithcm2021.hr_management.service.imp;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.ptithcm2021.hr_management.dto.request.AssignmentRequest;
//import org.ptithcm2021.hr_management.dto.response.AssignmentResponse;
//import org.ptithcm2021.hr_management.exception.AppException;
//import org.ptithcm2021.hr_management.exception.ErrorCode;
//import org.ptithcm2021.hr_management.mapper.AssignmentMapper;
//import org.ptithcm2021.hr_management.model.*;
//import org.ptithcm2021.hr_management.repository.DisciplineAssignmentRepository;
//import org.ptithcm2021.hr_management.repository.DisciplineDecisionRepository;
//import org.ptithcm2021.hr_management.repository.RewardAssignmentRepository;
//import org.ptithcm2021.hr_management.repository.RewardDecisionRepository;
//import org.ptithcm2021.hr_management.service.AssignmentService;
//import org.ptithcm2021.hr_management.service.UserService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("disciplineAssignmentServiceImpl")
//@RequiredArgsConstructor
//public class DisciplineAssignmentServiceImpl implements AssignmentService {
//    private final DisciplineDecisionRepository decisionRepository;
//    private final DisciplineAssignmentRepository assignmentRepository;
//    private final UserService userService;
//    private final AssignmentMapper assignmentMapper;
//
//    @Override
//    public AssignmentResponse createAssignment(AssignmentRequest request) {
//        DisciplineDecision decision = decisionRepository.findById(request.getDecisionId())
//                .orElseThrow(() -> new AppException(ErrorCode.DISCIPLINE_DECISION_NOT_FOUND));
//
//        User user = userService.getUserToUser(request.getUserId());
//
//        AssignmentId assignmentId = new AssignmentId(request.getUserId(), request.getDecisionId());
//        DisciplineAssignment assignment = new DisciplineAssignment(assignmentId, user, decision);
//
//        return assignmentMapper.toDisciplinedAssignmentResponse(assignmentRepository.save(assignment));
//    }
//
//    @Transactional
//    public AssignmentResponse updateAssignment(AssignmentRequest request, String rewardDecisionId, long userId) {
//        AssignmentId oldId = new AssignmentId(userId, rewardDecisionId);
//
//        // Tìm kiếm bản ghi cũ
//        DisciplineAssignment existingAssignment = assignmentRepository.findById(oldId)
//                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
//
//        // Xóa bản ghi cũ
//        assignmentRepository.delete(existingAssignment);
//
//        // Tạo bản ghi mới với userId mới
//        long newUserId = request.getUserId() != null? request.getUserId(): existingAssignment.getUser().getId();
//        String newDecision = request.getDecisionId() != null? request.getDecisionId(): existingAssignment.getDisciplineDecision().getId();
//
//        AssignmentId newId = new AssignmentId( newUserId, newDecision);
//
//        DisciplineAssignment newAssignment = new DisciplineAssignment();
//        newAssignment.setId(newId);
//
//        DisciplineDecision decision = decisionRepository.findById(newDecision)
//                .orElseThrow(() -> new AppException(ErrorCode.DISCIPLINE_DECISION_NOT_FOUND));
//        newAssignment.setDisciplineDecision(decision);
//
//        User newUser = userService.getUserToUser(newUserId);
//        newAssignment.setUser(newUser);
//
//        return assignmentMapper.toDisciplinedAssignmentResponse(assignmentRepository.save(newAssignment));
//    }
//
//
//    @Override
//    public AssignmentResponse getAssignment(String rewardId, long userId) {
//        AssignmentId temp = new AssignmentId(userId, rewardId);
//
//        DisciplineAssignment assignment = assignmentRepository.findById(temp)
//                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
//
//        return assignmentMapper.toDisciplinedAssignmentResponse(assignment);
//    }
//
//    @Override
//    public List<AssignmentResponse> getAllAssignmentByUser(long id) {
//        List<DisciplineAssignment> rewardAssignments = assignmentRepository.findAllByUserId(id)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//        return rewardAssignments.stream().map(assignmentMapper::toDisciplinedAssignmentResponse).toList();
//    }
//
//    @Override
//    public void deleteAssignment(String rewardId, long userId) {
//        AssignmentId temp = new AssignmentId(userId, rewardId);
//        if(assignmentRepository.existsById(temp)){
//            assignmentRepository.deleteById(temp);
//        } else{
//            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
//        }
//    }
//
//}
