//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.Date;
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
//    private String attachment;
//    private String content;
//    private Date date;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private User signer;
//}
