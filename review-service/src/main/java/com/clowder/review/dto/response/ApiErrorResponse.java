package com.clowder.review.dto.response;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message, String path, int status, LocalDateTime timestamp) {}
