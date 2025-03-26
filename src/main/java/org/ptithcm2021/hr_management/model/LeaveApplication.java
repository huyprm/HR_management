package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.enums.LeaveTypeEnum;

import java.util.Date;

@Entity(name = "leaveApplications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date startDate;
    private Date endDate;

    @Column(columnDefinition = "text")
    private String reason;

    private FormStatusEnum formStatusEnum;
    private LeaveTypeEnum isPaid;

    @ManyToOne
    @JoinColumn(name ="userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "leaveType")
    private LeaveType leaveType;
}
