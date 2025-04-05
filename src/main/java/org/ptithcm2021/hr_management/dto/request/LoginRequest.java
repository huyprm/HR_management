package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username cannot be empty")
    @Email(message = "Email is in wrong format")
    private String username;

    @NotBlank(message = "Password not empty")
    @Size(min = 5, message = "Password is greater than 5 characters")
    private String password;
}
