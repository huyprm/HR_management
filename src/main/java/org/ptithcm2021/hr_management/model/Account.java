package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Password not empty")
    private String password;

    @Column
    @Builder.Default
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    @OneToOne(mappedBy = "account")
    private User user;
}
