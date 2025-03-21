package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Department{
    @Id
    private String id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "department")
    private List<Position> positions;
}
