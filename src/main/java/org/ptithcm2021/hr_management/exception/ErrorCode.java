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
    REWARD_ASSIGNMENT_NOT_FOUND(1021, "Reward assignment not found", HttpStatus.NOT_FOUND),

    ;

    ErrorCode(int code, String message, HttpStatus status){
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
