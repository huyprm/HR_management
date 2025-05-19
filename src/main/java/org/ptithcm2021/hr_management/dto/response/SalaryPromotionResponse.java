package org.ptithcm2021.hr_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryPromotionResponse {
    private int id;
    private String date;
    private String status;
    private String note;
    private String reason;
    private String userName;
    private String signerName;
    private String currentJobGradeName;
    private String requestJobGradeName;
    private double requestJobGradeValue;
}
