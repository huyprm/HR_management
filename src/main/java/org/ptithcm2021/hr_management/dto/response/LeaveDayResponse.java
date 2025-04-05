package org.ptithcm2021.hr_management.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDayResponse {
    private int id;
    private Date date;
    private String name;
    private String description;
}
