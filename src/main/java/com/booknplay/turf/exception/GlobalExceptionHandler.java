package com.booknplay.turf.exception;

import com.booknplay.turf.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(TurfNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTurfNotFound(
            TurfNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(

            Exception ex,

            HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()

                .timestamp(LocalDateTime.now())

                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())

                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())

                .message("An unexpected error occurred")

                .path(request.getRequestURI())

                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                .body(errorResponse);

    }

    @ExceptionHandler(InvalidTurfException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTurf(
            InvalidTurfException ex,
            HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
