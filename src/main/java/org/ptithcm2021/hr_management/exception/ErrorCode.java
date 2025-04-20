package org.ptithcm2021.hr_management.exception;

import lombok.Getter;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED (1000, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_JWT (1001, "Jwt invalid", HttpStatus.UNAUTHORIZED),
    USERNAME_NOT_FOUND(1002, "Username does not existed", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD(1003, "Wrong password", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(1004, "Account locked", HttpStatus.LOCKED),
    DEGREE_NOT_FOUND(1005, "Degree not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1006, "Role not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1007, "User not found", HttpStatus.NOT_FOUND),
    PASSWORD_NOT_MATCH(1008, "Passwords do not match", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(1009, "Department not found", HttpStatus.NOT_FOUND),
    POSITION_NOT_FOUND(1010," Position not found", HttpStatus.NOT_FOUND),
    CANNOT_BE_DELETED(1011, "Cannot delete record because related data still exists", HttpStatus.CONFLICT),
    CONTRACT_TYPE_NAME_EXISTED(1012, "The name of the contract type already exists", HttpStatus.CONFLICT),
    CONTRACT_TYPE_NOT_FOUND(1013, "Contract type not found", HttpStatus.NOT_FOUND),
    DEPARTMENT_NAME_EXIST(1014, "The department name already exists", HttpStatus.CONFLICT),
    POSITION_NAME_EXISTS(1015,"Position name already exists", HttpStatus.CONFLICT),
    JOB_GRADE_NAME_EXISTS(1016, "Job grade name already exists", HttpStatus.CONFLICT),
    JOB_GRADE_NOT_FOUND(1017, "Job grade not found", HttpStatus.NOT_FOUND),
    FEEDBACK_NOT_FOUND(1018, "Feedback not found", HttpStatus.NOT_FOUND),
    USER_TERMINATED(1019,"User has terminated the contract", HttpStatus.BAD_REQUEST),
    NOTIFICATION_NOT_FOUND(1020, "Notification not found", HttpStatus.NOT_FOUND),
    REWARD_DECISION_NOT_FOUND(1021, "Reward decision not found", HttpStatus.NOT_FOUND),
    ASSIGNMENT_NOT_FOUND(1022, "Reward assignment not found", HttpStatus.NOT_FOUND),
    DISCIPLINE_DECISION_NOT_FOUND(1023, "Discipline decision not found", HttpStatus.NOT_FOUND),
    LEAVE_TYPE_NOT_FOUND(1024, "Leave type not found", HttpStatus.NOT_FOUND),
    LEAVE_APPLICATION_NOT_FOUND(1025, "Leave application not found", HttpStatus.NOT_FOUND),
    LEAVE_BALANCE_NOT_FOUND(1026, "Leave balance not found", HttpStatus.NOT_FOUND),
    LEAVE_DAY_NOT_FOUND(1027, "Leave day not found", HttpStatus.NOT_FOUND),
    SENIORITY_ALLOWANCE_RULE_NOT_FOUND(1028, "Seniority allowance rule not found", HttpStatus.NOT_FOUND),
    CONTRACT_NOT_FOUND(1029, "Contract not found", HttpStatus.NOT_FOUND),
    EXTEND_CONTRACT(1030, "The contract is still valid and cannot be renewed.", HttpStatus.CONFLICT),
    CONTRACT_OVERLAP(1031, "Contract overlap",HttpStatus.CONFLICT),
    EMAIL_NOT_FOUND(1032,"Email does not exist" ,HttpStatus.NOT_FOUND),
    JOB_GRADE_ID_EXISTS(1033,"Job grade id exists" ,HttpStatus.CONFLICT ),
    POSITION_ID_EXISTS(1034, "Position id exists",HttpStatus.CONFLICT ),
    DEPARTMENT_ID_EXIST(1035,"Department id exists" , HttpStatus.CONFLICT),
    CONTRACT_NOT_ELIGIBLE_FOR_RENEWAL(1036, "Contract not eligible for renewal",HttpStatus.CONFLICT ),
    INVALID_DECISION_TYPE(1037,"Invalid decision type" , HttpStatus.BAD_REQUEST ),
    DECISION_ALREADY_EXISTS(1038, "Decision already exists",HttpStatus.BAD_REQUEST),
    DECISION_NOT_FOUND(1039, "Decision not found", HttpStatus.NOT_FOUND ),
    SALARY_PROMOTION_NOT_FOUND(1040, "Salary promotion not found", HttpStatus.NOT_FOUND),
    SALARY_NOT_FOUND(1041, "Salary not found", HttpStatus.NOT_FOUND),
    SALARY_ALREADY_EXISTS(1042, "Salary for this month already exists", HttpStatus.CONFLICT),
    INVALID_CONTRACT_USER(1043, "The contract does not belong to the specified user", HttpStatus.BAD_REQUEST),
    CONTRACT_INVALID_STATUS(1044,"Contract invalid status" ,HttpStatus.BAD_REQUEST),
    PROMOTION_ALREADY_PROCESSED(1045, "The promotion request has already been processed", HttpStatus.CONFLICT),
    CONTRACT_UPDATE_FAILED(1046, "Failed to update contract with new salary and job grade", HttpStatus.INTERNAL_SERVER_ERROR),
    DECISION_CREATION_FAILED(1047, "Failed to create salary change decision", HttpStatus.INTERNAL_SERVER_ERROR),
    LEAVE_TYPE_ALREADY_EXISTS(1048, "Leave type already exists", HttpStatus.BAD_REQUEST),
    FORM_STATUS_INVALID(1049,"Form status invalid" ,HttpStatus.BAD_REQUEST ),
    INVALID_LEAVE_APPLICATION(1050, "Invalid leave application data", HttpStatus.BAD_REQUEST),
    INVALID_MONTH(1051, "Invalid month value", HttpStatus.BAD_REQUEST),
    MONTH_YEAR_LEAVE_BALANCE_EXISTS(1052, "Leave balance for this month and year already exists", HttpStatus.CONFLICT),
    LEAVE_BALANCE_EXCEEDED(1053, "Leave balance exceeded allowed days", HttpStatus.BAD_REQUEST),
    RIGHT_SIGNER(1054,"The signatory is not authorized to sign." ,HttpStatus.BAD_REQUEST );

    ErrorCode(int code, String message, HttpStatus status){
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
