package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Account {
    @Id
    private String username;
    private String password;
    @Column
    @Builder.Default
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;
}
