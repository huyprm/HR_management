package org.ptithcm2021.hr_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionRequest {
    @NotBlank(message = "Decision code cannot be empty")
    private String id;

    @NotBlank(message = "Decision type cannot be empty")
    private String type;

    private String content;

    @NotNull(message = "Decision date cannot be empty")
    private Date date;

    @NotNull(message ="The decision maker cannot leave blank.")
    private long signerId;
}
