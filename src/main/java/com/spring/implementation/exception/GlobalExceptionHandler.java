package com.spring.implementation.exception;


import com.spring.implementation.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse()
            .status(ex.getStatus().value())
            .error(ex.getStatus().getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI());

        return ResponseEntity
            .status(ex.getStatus())
            .body(body);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse()
            .status(ex.getStatus().value())
            .error(ex.getStatus().getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI());

        return ResponseEntity
            .status(ex.getStatus())
            .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOthers(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse body = new ErrorResponse()
            .status(500)
            .error("Internal Server Error")
            .message(ex.getMessage())
            .path(request.getRequestURI());

        return ResponseEntity
            .status(500)
            .body(body);
    }
}