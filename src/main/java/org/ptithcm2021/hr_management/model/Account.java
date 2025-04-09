package org.ptithcm2021.hr_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ptithcm2021.hr_management.enums.RoleEnum;

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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoleEnum role = RoleEnum.USER;

    @OneToOne(mappedBy = "account")
    private User user;
}
