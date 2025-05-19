package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.ptithcm2021.hr_management.enums.RoleEnum;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    private RoleEnum id;
    private String description;
    private int level;

}
