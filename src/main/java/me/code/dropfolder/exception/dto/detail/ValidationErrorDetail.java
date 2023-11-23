package me.code.dropfolder.exception.dto.detail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A data class representing a validation error.
 * It extends the ErrorDetail class and provides additional information specific to validation errors.
 */
@Getter
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ValidationErrorDetail extends ErrorDetail {

    private String targetObject;
    private String targetField;
    private Object rejectedValue;
    private String message;

}
