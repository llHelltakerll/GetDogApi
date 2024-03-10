package com.example.getdog.controller;

import com.example.getdog.util.ApiIsExistException;
import com.example.getdog.util.ApiNotFoundException;
import com.example.getdog.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class DogApiBaseController {
    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(ApiNotFoundException ex) {
        return ApiResponse.buildResponse(HttpStatus.NOT_FOUND, "error", ex.getMessage());
    }

    @ExceptionHandler(ApiIsExistException.class)
    public ResponseEntity<?> handleConflictException(ApiIsExistException ex) {
        return ApiResponse.buildResponse(HttpStatus.CONFLICT, "error", ex.getMessage());
    }
}
