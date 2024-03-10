package com.example.getdog.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    public static ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String statusMsg, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", status.value());
        response.put("message", message);
        response.put("status", statusMsg);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String statusMsg, Object obj) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", status.value());
        response.put("object", obj);
        response.put("status", statusMsg);
        return ResponseEntity.status(status).body(response);
    }

}

