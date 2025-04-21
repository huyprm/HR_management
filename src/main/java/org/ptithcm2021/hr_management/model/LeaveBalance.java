package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leaveBalances", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "year", "month"})})
@Builder
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int year;
    
    @Column
    private int month;

    private int totalLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;
    private int remainingLeaveDay;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;

    public YearMonth getYearMonth() {
        return YearMonth.of(year, month);
    }
}
