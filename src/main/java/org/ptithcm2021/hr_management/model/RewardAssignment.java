//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Date;
//
//@Entity(name = "rewardAssignments")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RewardAssignment {
//    @Id
//    private RewardAssigmentId id;
//
//    @ManyToOne
//    @MapsId("employeeId")
//    @JoinColumn(name ="rewardDecisionId")
//    private RewardDecision rewardDecision;
//
//    @ManyToOne
//    @MapsId("rewardDecisionId")
//    @JoinColumn(name ="userId")
//    private User user;
//}
