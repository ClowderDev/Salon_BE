package com.clowder.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

  @NotBlank(message = "Category name is required")
  private String name;

  @NotNull(message = "Salon ID is required")
  private Long salonId;
}
