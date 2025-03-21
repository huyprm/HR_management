package org.ptithcm2021.hr_management.exception;

public class AppException extends RuntimeException{
    private ErrorCode errorCode;

    public AppException( ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
