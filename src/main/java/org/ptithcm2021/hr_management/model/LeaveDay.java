package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.LeaveDayTypeEnum;

import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leaveDays", indexes = {
        @Index(name = "idx_leave_day_date", columnList = "date")
})
public class LeaveDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @Temporal(TemporalType.DATE)
    private Date date;
    private String name;

    @Column(columnDefinition = "text")
    private String description;
}
