package com.clowder.dto.request;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalonDTO {

  private Long id;

  private String name;

  private List<String> images;

  private String address;

  private String phoneNumber;

  private String email;

  private String city;

  private Long ownerId;

  private LocalTime openingTime;

  private LocalTime closingTime;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
