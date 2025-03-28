//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.ptithcm2021.hr_management.enums.ContractStatusEnum;
//
//import java.util.Date;
//import java.util.List;
//
//@Entity(name = "contractTypes")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ContractType {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    private String name;
//    private Date startDate;
//    private Date endDate;
//    private ContractStatusEnum contractStatusEnum;
//    private double baseSalary;
//
//    @OneToOne
//    @JoinColumn(name = "userId")
//    private User user;
//
//    @OneToMany(mappedBy = "contractType")
//    private List<Contract> contracts;
//
//    @ManyToOne
//    @JoinColumn(name = "positionId")
//    private Position position;
//
//    @ManyToOne
//    @JoinColumn(name = "jobGradeId")
//    private JobGrade jobGrade;
//
//}
