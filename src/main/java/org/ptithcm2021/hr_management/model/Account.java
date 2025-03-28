package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 5, message = "Password is greater than 5 characters")
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
