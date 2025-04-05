package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.Year;
import java.util.Date;

@Entity(name = "leaveBalances")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "year"})})
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int year = Year.now().getValue();

    private int totalLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;

    public int getRemainingLeave() {
        return totalLeaveDay + carriedOverDay - usedLeaveDay;
    }

}
