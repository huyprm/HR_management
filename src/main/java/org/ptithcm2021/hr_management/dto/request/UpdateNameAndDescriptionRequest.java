package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNameAndDescriptionRequest {
    @NotBlank(message = "Position name cannot be blank")
    private String name;

    private String description;
}
