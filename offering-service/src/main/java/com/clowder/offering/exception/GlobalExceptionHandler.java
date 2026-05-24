package com.clowder.offering.exception;

import com.clowder.booking.dto.response.ApiErrorResponse;
import com.clowder.booking.exception.ResourceNotFoundException;
import feign.FeignException;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
      ResourceNotFoundException ex, WebRequest request) {
    log.warn("Resource not found: {}", ex.getMessage());
    return buildResponse(ex.getMessage(), request, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex, WebRequest request) {
    log.warn("Business error: {}", ex.getMessage());
    return buildResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
      IllegalArgumentException ex, WebRequest request) {
    log.warn("Bad request: {}", ex.getMessage());
    return buildResponse(ex.getMessage(), request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
      MethodArgumentNotValidException ex, WebRequest request) {
    String message =
        ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .reduce((a, b) -> a + "; " + b)
            .orElse("Validation failed");
    log.warn("Validation error: {}", message);
    return buildResponse(message, request, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ApiErrorResponse> handleFeign(FeignException ex, WebRequest request) {
    log.error("Feign client error: {}", ex.getMessage());
    return buildResponse(
        "Service communication error: " + ex.getMessage(), request, HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, WebRequest request) {
    log.error("Unexpected error", ex);
    return buildResponse("Internal server error", request, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ApiErrorResponse> buildResponse(
      String message, WebRequest request, HttpStatus status) {
    ApiErrorResponse response =
        new ApiErrorResponse(
            message, request.getDescription(false), status.value(), LocalDateTime.now());
    return ResponseEntity.status(status).body(response);
  }
}
