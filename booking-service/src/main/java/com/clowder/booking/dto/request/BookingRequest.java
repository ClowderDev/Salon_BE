package com.clowder.booking.dto.request;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Set<Long> serviceIds;
}
