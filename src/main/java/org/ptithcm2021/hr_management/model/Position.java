package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "positions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Position {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

}
