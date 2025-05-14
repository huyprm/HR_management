package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_grades")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobGrade {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;
    private double coefficient;
    private String description;
}
