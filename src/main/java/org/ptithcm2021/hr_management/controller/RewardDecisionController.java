//package org.ptithcm2021.hr_management.controller;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.ptithcm2021.hr_management.dto.request.DecisionRequest;
//import org.ptithcm2021.hr_management.dto.response.ApiResponse;
//import org.ptithcm2021.hr_management.dto.response.DecisionResponse;
//import org.ptithcm2021.hr_management.service.DecisionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/reward-decisions")
//public class RewardDecisionController {
//    private final DecisionService decisionService;
//
//    @Autowired
//    public RewardDecisionController(@Qualifier(value = "rewardDecisionServiceImpl")DecisionService decisionService){
//        this.decisionService = decisionService;
//    }
//
//    @PostMapping("/create")
//    public ApiResponse<DecisionResponse> createRewardDecision(@RequestBody @Valid DecisionRequest decisionRequest) {
//        return ApiResponse.<DecisionResponse>builder()
//                .data(decisionService.createDecision(decisionRequest)).build();
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<DecisionResponse> updateRewardDecision(@PathVariable String id, @RequestBody @Valid DecisionRequest decisionRequest) {
//        return ApiResponse.<DecisionResponse>builder()
//                .data(decisionService.updateDecision(id, decisionRequest)).build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteRewardDecision(@PathVariable String id) {
//        decisionService.deleteDecision(id);
//        return ApiResponse.<Void>builder().message("Deleted successfully").build();
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<DecisionResponse> getRewardDecision(@PathVariable String id) {
//        return ApiResponse.<DecisionResponse>builder()
//                .data(decisionService.getDecision(id)).build();
//    }
//
//    @GetMapping()
//    public ApiResponse<List<DecisionResponse>> getAllRewardDecisions() {
//        return ApiResponse.<List<DecisionResponse>>builder()
//                .data(decisionService.getAllDecision()).build();
//    }
//}
