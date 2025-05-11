package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "leaveTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    // Ví dụ: true cho nghỉ phép thường, false cho nghỉ BHXH, nghỉ ốm có giấy bác sĩ, v.v.
    @Builder.Default
    private boolean affectLeaveBalance = false;

}
