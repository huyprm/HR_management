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
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int year = Year.now().getValue();
    
    @Column
    private int month = LocalDate.now().getMonthValue();

    private int totalLeaveDay;
    private int carriedOverDay;
    private int usedLeaveDay;
    private int usedBHXH;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;

    public int getRemainingLeave() {
        return totalLeaveDay + carriedOverDay - usedLeaveDay;
    }

    public YearMonth getYearMonth() {
        return YearMonth.of(year, month);
    }
}
