//package org.ptithcm2021.hr_management.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.Instant;
//
//@Entity(name = "notifications")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Notification {
//    @Id
//    private long id;
//
//    private String title;
//    private String content;
//    private Instant sendDate;
//
//    @ManyToOne
//    @JoinColumn(name = "userId")
//    private User sender;
//}
