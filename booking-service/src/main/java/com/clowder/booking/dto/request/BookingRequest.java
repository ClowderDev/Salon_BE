package com.clowder.booking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

  @NotNull(message = "Salon ID is required")
  private Long salonId;

  @NotNull(message = "Start time is required")
  @Future(message = "Start time must be in the future")
  private LocalDateTime startTime;

  @NotNull(message = "End time is required")
  @Future(message = "End time must be in the future")
  private LocalDateTime endTime;

  @NotEmpty(message = "At least one service must be selected")
  private Set<Long> servicesIds;
}
