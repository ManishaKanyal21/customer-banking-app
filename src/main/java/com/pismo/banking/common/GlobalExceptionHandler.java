package com.pismo.banking.common;

import com.pismo.banking.common.exception.AccountNotFoundException;
import com.pismo.banking.common.exception.InSufficientLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * A global exception handler that intercepts exceptions thrown by controllers across the entire application
 * and translates them into standardized, client-friendly {@link ApiError} responses.
 *
 * <p>It provides specific handling for validation errors, expected business exceptions (like Entity Not Found),
 * and a general fallback for unexpected runtime errors.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions thrown when method arguments annotated with @Valid or @Validated fail validation constraints.
     * Translates validation failures into an HTTP 400 Bad Request response.
     *
     * @param ex The MethodArgumentNotValidException thrown by the Spring framework.
     * @return A {@link ResponseEntity} containing a detailed {@link ApiError} response and HTTP 400 status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(httpStatus, "Validation failed: " + errorMessage);
        return new ResponseEntity<>(apiError, httpStatus);
    }

    /**
     * A general fallback handler for any {@link RuntimeException} that hasn't been handled by a more specific
     * {@code @ExceptionHandler}.
     * Translates unexpected server errors into an HTTP 500 Internal Server Error response.
     *
     * @param exception The unhandled runtime exception.
     * @return A {@link ResponseEntity} containing a generic {@link ApiError} response and HTTP 500 status.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleGenericRuntimeException(RuntimeException exception) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError apiError = new ApiError(httpStatus, exception.getMessage());
        return new ResponseEntity<>(apiError, httpStatus);
    }

    /**
     * Handles {@link AccountNotFoundException}, translating this specific business exception
     * into an appropriate HTTP 404 Not Found response.
     *
     * @param accountNotFoundException The exception thrown when a requested account cannot be found.
     * @return A {@link ResponseEntity} containing an {@link ApiError} response and HTTP 404 status.
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFoundException
            (final AccountNotFoundException accountNotFoundException) {
        final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ApiError apiError = new ApiError(httpStatus, accountNotFoundException.getMessage());
        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(InSufficientLimitException.class)
    public ResponseEntity<ApiError> handleInSufficientLimitException
            (final InSufficientLimitException inSufficientLimitException) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(httpStatus, inSufficientLimitException.getMessage());
        return ResponseEntity.status(httpStatus).body(apiError);
    }
}
