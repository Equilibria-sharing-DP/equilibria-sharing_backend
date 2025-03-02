package api.equilibria_sharing.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), HttpStatus.CONFLICT.value(), ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred."
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String message;

        public ErrorResponse(LocalDateTime timestamp, int status, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.message = message;
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public String getMessage() { return message; }
    }
}