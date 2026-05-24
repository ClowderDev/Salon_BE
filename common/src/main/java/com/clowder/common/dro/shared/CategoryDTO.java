package com.clowder.common.dto.shared;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
  private Long id;
  private String name;
  private String image;
}
