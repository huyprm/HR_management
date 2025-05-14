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
@Table(name = "leave_balances", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "year", "month"})})
@Builder
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int year;
    
    @Column
    private int month;

    private double totalLeaveDay;
    private double carriedOverDay;
    private double usedLeaveDay;
    private double remainingLeaveDay;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    public YearMonth getYearMonth() {
        return YearMonth.of(year, month);
    }
}
