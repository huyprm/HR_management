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
//@Entity(name = "seniorityAllowanceRules")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class SeniorityAllowanceRule {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @Column(nullable = false)
//    private int minService;
//
//    @Column(nullable = false)
//    private double seniorityPercentage;
//
//    @Column(nullable = false)
//    private int seniorityLeaveDay;
//
//    @Column(nullable = false)
//    private Date effectiveDate;
//
//    private Date expiryDate;
//
//    @Column(columnDefinition = "text")
//    private String description;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private User signer;
//}
