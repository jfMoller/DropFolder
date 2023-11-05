package me.code.dropfolder.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A data class representing a validation error.
 * It extends the ErrorDetail class and provides additional information specific to validation errors.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ValidationErrorDetail extends ErrorDetail {

    // The object associated with the validation error.
    private String targetObject;


    // The field within the object that caused the validation error.
    private String targetField;


    // The value that was rejected during validation.
    private Object rejectedValue;


    // The error message describing the validation failure.
    private String message;

}
