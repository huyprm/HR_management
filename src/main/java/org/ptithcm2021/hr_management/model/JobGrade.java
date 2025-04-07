package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "jobGrades")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobGrade {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;
    private double coefficient;
    private String description;
}
