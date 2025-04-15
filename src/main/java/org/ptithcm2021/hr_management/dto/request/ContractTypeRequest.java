package org.ptithcm2021.hr_management.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractTypeRequest {
    @NotBlank(message = "Contract type code cannot be empty")
    private String id;

    @NotBlank(message = "Contract type name cannot be empty")
    private String name;

    private String duration;

    //@NotNull(message = "Are employees entitled to the policy?")
    private boolean isPolicy;
}
