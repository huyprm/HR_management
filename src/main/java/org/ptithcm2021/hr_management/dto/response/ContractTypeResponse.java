package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTypeResponse {
    private int id;

    private String name;

    private String duration;
}
