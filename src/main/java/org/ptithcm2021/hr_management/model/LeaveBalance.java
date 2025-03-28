//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import lombok.*;
//
//import java.time.Year;
//
//@Entity(name = "leaveBalances")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class LeaveBalance {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private Year year;
//    private int totalLeaveDay;
//    private int carriedOverDay;
//    private int usedLeaveDay;
//    private int remainingLeaveDay;
//
//    @ManyToOne
//    @JoinColumn(name ="userId")
//    private User user;
//
//}
