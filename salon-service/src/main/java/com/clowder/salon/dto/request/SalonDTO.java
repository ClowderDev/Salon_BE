package com.clowder.salon.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalonDTO {

  private Long id;

  @NotBlank(message = "Salon name is required")
  private String name;

  private List<String> images;

  @NotBlank(message = "Address is required")
  private String address;

  @NotBlank(message = "Phone number is required")
  private String phoneNumber;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "City is required")
  private String city;

  private Long ownerId;

  @NotNull(message = "Opening time is required")
  private LocalTime openingTime;

  @NotNull(message = "Opening time is required")
  private LocalTime closingTime;
}
