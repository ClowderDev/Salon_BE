package com.clowder.common.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message, String path, int status, LocalDateTime timestamp) {}
