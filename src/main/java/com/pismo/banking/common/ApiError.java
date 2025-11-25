package com.pismo.banking.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) used to represent standardized error responses sent back to the client
 * when an exception occurs within the API controllers.
 */
@Schema(description = "Standardized API error response structure")
public record ApiError(
        @Schema(description = "The HTTP status code")
        int status,
        @Schema(description = "Reason phrase for the HTTP status code")
        String error,
        @Schema(description = "A description of the error")
        String message,
        @Schema(description = "The timestamp when the error occurred")
        LocalDateTime timestamp) {

    /**
     * Constructs a new ApiError instance. The timestamp is automatically set to the current time.
     *
     * @param status The HTTP status enumeration (e.g., BAD_REQUEST, NOT_FOUND, CONFLICT).
     * @param message A descriptive error message detailing what went wrong.
     */
    public ApiError(HttpStatus status, String message) {
        this(status.value(), status.getReasonPhrase(), message, LocalDateTime.now());
    }
}
