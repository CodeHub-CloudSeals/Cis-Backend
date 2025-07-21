package com.spring.implementation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when trying to create a resource that already exists.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends ApiException {

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(
            HttpStatus.CONFLICT,
            String.format("%s already exists with %s = %s", resourceName, fieldName, fieldValue)
        );
    }
}