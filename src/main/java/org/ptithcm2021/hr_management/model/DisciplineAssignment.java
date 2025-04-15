//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.io.Serializable;
//import java.util.Date;
//
//@Entity(name = "disciplineAssignments")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class DisciplineAssignment {
//    @EmbeddedId
//    private AssignmentId id;
//
//    @ManyToOne
//    @MapsId("userId")
//    @JoinColumn(name ="user_id")
//    private User user;
//
//
//    @ManyToOne
//    @MapsId("decisionId")
//    @JoinColumn(name ="decision_id")
//    private DisciplineDecision disciplineDecision;
//}
