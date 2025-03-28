//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.ptithcm2021.hr_management.enums.FormStatusEnum;
//
//import java.util.Date;
//
//@Entity(name = "salaryPromotionRequests")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class SalaryPromotionRequest {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String requestType;
//    private Date date;
//    private FormStatusEnum status;
//
//    @Column(columnDefinition = "text")
//    private String note;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "currentJobGradeId")
//    private JobGrade currentJobGrade;
//
//    @ManyToOne
//    @JoinColumn(name = "jobGradeId")
//    private JobGrade requestJobGrade;
//
//
//}
