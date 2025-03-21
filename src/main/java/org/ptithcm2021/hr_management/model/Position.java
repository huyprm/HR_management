package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "positions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

}
