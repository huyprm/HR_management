package org.ptithcm2021.hr_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.ptithcm2021.hr_management.enums.RoleEnum;

@Entity(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Role {
    @Id
    private RoleEnum id;
    private String description;

}
