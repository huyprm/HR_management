package org.ptithcm2021.hr_management.exception;

import org.ptithcm2021.hr_management.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(400);
        apiResponse.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e){
        ApiResponse apiResponse = new ApiResponse();

        ErrorCode errorCode = e.getErrorCode();

        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setCode(errorCode.getCode());

        return ResponseEntity.status(errorCode.getStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setMessage(e.getBindingResult().getFieldError().getDefaultMessage());
        apiResponse.setCode(400);

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
