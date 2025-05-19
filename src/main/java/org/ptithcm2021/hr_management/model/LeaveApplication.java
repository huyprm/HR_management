package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;
import org.ptithcm2021.hr_management.enums.LeaveTypeEnum;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "leave_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class LeaveApplication{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Column(columnDefinition = "text")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FormStatusEnum formStatusEnum = FormStatusEnum.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="signer_id")
    private User signer;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type")
    private LeaveType leaveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
