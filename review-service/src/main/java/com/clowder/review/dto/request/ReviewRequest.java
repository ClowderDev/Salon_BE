package com.clowder.review.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

  @NotBlank(message = "Review text is required")
  private String reviewText;

  @NotNull(message = "Rating is required")
  @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
  @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
  private Double rating;
}
