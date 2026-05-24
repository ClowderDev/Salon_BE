package com.clowder.offering.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDTO {

  private Long id;

  @NotBlank(message = "Service name is required")
  private String name;

  @NotBlank(message = "Description is required")
  private String description;

  @Min(value = 0, message = "Price must be non-negative")
  private int price;

  @Min(value = 1, message = "Duration must be at least 1 minute")
  private int duration;

  @NotNull(message = "Category ID is required")
  private Long categoryId;

  private String image;
}
