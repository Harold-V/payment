package tech.xirius.payment.infrastructure.web.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp) {
    public static ApiErrorResponse of(int status, String error, String message) {
        return new ApiErrorResponse(status, error, message, LocalDateTime.now());
    }
}
