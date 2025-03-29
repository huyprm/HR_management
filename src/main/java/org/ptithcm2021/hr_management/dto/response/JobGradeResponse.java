package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobGradeResponse {
    private String id;
    private String name;
    //private String groupName;
    private double coefficient;
    private String description;
}
