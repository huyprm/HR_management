package org.ptithcm2021.hr_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

@Entity(name = "jobGraces")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String groupName;
    private double coefficient;
    private String description;
}
