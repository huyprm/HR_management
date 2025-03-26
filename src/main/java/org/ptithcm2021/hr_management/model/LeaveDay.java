package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.LeaveDayTypeEnum;

import java.util.Date;

@Entity(name = "leaveDays")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date date;
    private String name;
    private LeaveDayTypeEnum leaveDayTypeEnum;

    @Column(columnDefinition = "text")
    private String description;


}
