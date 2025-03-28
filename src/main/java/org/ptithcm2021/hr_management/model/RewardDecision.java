//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.ptithcm2021.hr_management.enums.RewardDisciplineEnum;
//
//import java.util.Date;
//import java.util.List;
//
//@Entity(name = "rewardDecisions")
//@NoArgsConstructor
//@AllArgsConstructor
//@Setter
//@Getter
//public class RewardDecision {
//    @Id
//    private String id;
//
//    private String type;
//    private String content;
//    private Date date;
//
//    @OneToOne
//    @JoinColumn(name = "userId")
//    private User signer;
//
//}
