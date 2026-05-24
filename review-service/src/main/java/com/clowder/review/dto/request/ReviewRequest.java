package com.clowder.booking.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

  private String reviewText;
  private double rating;
}
