package org.ptithcm2021.hr_management.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = StringNumberValidator.class) // Liên kết với class Validator
@Target({ ElementType.FIELD, ElementType.PARAMETER }) // Áp dụng cho thuộc tính hoặc tham số
@Retention(RetentionPolicy.RUNTIME) // Annotation hoạt động ở runtime
public @interface StringNumberConstraint {
    String message() default "Chuối số không hợp lệ"; // Thông báo lỗi mặc định
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    NumberType type();

    enum NumberType {
        CCCD, PHONE
    }
}
