package com.clowder.exception;

import com.clowder.dto.response.ExceptionResponse;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> exceptionHandler(Exception ex, WebRequest request) {
    ExceptionResponse exceptionResponse = new ExceptionResponse(
        ex.getMessage(),
        request.getDescription(false),
        LocalDateTime.now()
    );

    return ResponseEntity.ok(exceptionResponse);

  }
}
