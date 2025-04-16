package org.ptithcm2021.hr_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ptithcm2021.hr_management.dto.request.SalaryRequest;
import org.ptithcm2021.hr_management.dto.response.SalaryResponse;
import org.ptithcm2021.hr_management.model.Salary;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface SalaryMapper {
    
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    Salary toSalary(SalaryRequest salaryRequest);
    
    @Mapping(target = "salaryMonth", source = "salaryMonth", qualifiedByName = "formatYearMonth")
    @Mapping(target = "paymentDate", source = "paymentDate", qualifiedByName = "formatDate")
    @Mapping(target = "totalSalary", expression = "java(calculateTotalSalary(salary))")
    SalaryResponse toSalaryResponse(Salary salary);
    
    @Named("formatYearMonth")
    default String formatYearMonth(YearMonth yearMonth) {
        if (yearMonth == null) {
            return null;
        }
        return yearMonth.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }
    
    @Named("formatDate")
    default String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
    
    default double calculateTotalSalary(Salary salary) {
        double coefficient = salary.getContract().getJobGrade().getCoefficient();
        return (salary.getBaseSalary() * coefficient) + salary.getTotalAllowance() - salary.getUnpaidLeaveDeduction();
    }
}