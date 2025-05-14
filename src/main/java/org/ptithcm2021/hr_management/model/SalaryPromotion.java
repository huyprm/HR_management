package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.hr_management.enums.FormStatusEnum;

import java.util.Date;

@Entity
@Table(name = "salary_promotion_requests")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SalaryPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date = new Date();

    @Enumerated(EnumType.STRING)
    private FormStatusEnum status = FormStatusEnum.PENDING;

    @Column(columnDefinition = "text")
    private String reason;

    @Column(columnDefinition = "text")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signer_id")
    private User signer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_job_grade_id")
    private JobGrade currentJobGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_grade_id")
    private JobGrade requestJobGrade;


}
