package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "leaveTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    // Ví dụ: true cho nghỉ phép thường, false cho nghỉ BHXH, nghỉ ốm có giấy bác sĩ, v.v.
    private boolean affectLeaveBalance = true;

    // Ví dụ: true cho nghỉ không lương, BHXH; false cho nghỉ phép có lương
    //private boolean affectSalary = true;

    // Ví dụ: 0.0 = không giảm lương, 0.5 = giảm 50% lương, 1.0 = không được trả lương
    //private double salaryDeductionRate = 1.0;
}
