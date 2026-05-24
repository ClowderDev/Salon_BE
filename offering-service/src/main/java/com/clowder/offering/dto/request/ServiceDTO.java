package com.clowder.booking.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceDTO {

  private Long id;

  private String name;

  private String description;

  private String price;

  private int duration;

  private Long salonId;

  private Long categoryId;

  private String image;
}
